// Changing the channel of the user if user.save() called from changeOfCountry class (changeOfChannel = true) or if International is turned on for the first time
Parse.Cloud.beforeSave(Parse.User, function(request,response){
	Parse.Cloud.useMasterKey();
	var changeOfChannel = request.object.get("changeOfChannel");
	var international = request.object.get("international");	

	var maxMembers = 2; // Setting the maximum number of members per channel

	if (international && typeof request.object.get("internationalChannel") === "undefined") { // International option is turned on for the first time
		var query = new Parse.Query("Country");
		query.get("7wQb7cZnQH", {
		  success: function(resultCountry) {
			var channelQuery = new Parse.Query("Channel");
				channelQuery.equalTo("country","international");
				channelQuery.notEqualTo("full", true);
				channelQuery.first().then(function(resultChannel) { // Querying the first matching, unfull, channel
					
					if (resultChannel != null) { // If there is one
					var m = resultChannel.get("members");
					var max = resultChannel.get("maxMembers");
					var channelName = resultChannel.get("name");
					resultChannel.set("members", m + 1); // Updating the number of members
						if (m + 1 == max) {
							resultChannel.set("full", true); // If the maximum number of members is reached, full = true
							resultCountry.set("nbrUsers",nbrUsers+1); // Updating the number of users for the "country" International
							if (resultChannel.get("counted") == false){ // If it's the first time the channel gets full
							resultCountry.set("nbrChannels",nbrChannels + 1); // One more channel for International
							resultChannel.set("counted",true); // The channel is now counted (it has been full at least once)
							} 
							resultCountry.set("avUsersPerChannel",Math.floor((nbrUsers+1)/(nbrChannels+1)));// Updating "country" International's average number of users per channel
						} else {
							resultCountry.set("nbrUsers",nbrUsers+1); // If the maximum numbers is not reached, only the number of users for International is updated
						}
					request.object.set("internationalChannel",channelName); // Setting the user's international channel
					return Parse.Object.saveAll([resultChannel,resultCountry]); // Saving "country" international and channel
					
					} else { // If all the matching channels are full: we need to create a new one
					
					var newChannel = new Parse.Object("Channel"); // Creation of a new channel
					newChannel.set("name", "international_" + nbrChannels); // Naming it
					newChannel.set("full",false); // Not full
					newChannel.set("country","international"); // International Channel
					newChannel.set("members",1); // Only one user
					newChannel.set("lastTester",0); // USELESS?
					newChannel.set("maxMembers",maxMembers); // Setting the maximum number of members
					newChannel.set("counted",false); // Has never been full, so not counted yet
					resultCountry.set("nbrUsers",nbrUsers+1); // Updating "country" International's number of users
					resultCountry.set("avUsersPerChannel",Math.floor((nbrUsers+1)/(nbrChannels+1))); // Updating average number of users per channel
																									 // (no new channel because not counted)
					request.object.set("internationalChannel","international_" + nbrChannels); // Setting the user's international channel
					return Parse.Object.saveAll([newChannel,resultCountry]); // Saving "country" international and channel
					
					}
				}, function(error){
					console.error("there was an error" + error);
				});
		  },
		  error: function(object, error) {
		  }
		});
	}
	
	if (changeOfChannel) { // There is a change of channel
		
		var oldChannelName = request.object.get("channel"); // We need to update the number of members of the user's former channel and country

		if (oldChannelName != null) { // If it's not the first time the user chooses his country (ie) he already has a channel
			
			var oldChannelQuery = new Parse.Query("Channel"); // Query for the old channel
			oldChannelQuery.equalTo("name",oldChannelName);
			oldChannelQuery.first().then(function(oldChannel) {
				
				var oldChannelMembers = oldChannel.get("members");
				var oldChannelCountry = oldChannel.get("country");

				var oldCountryQuery = new Parse.Query("Country"); // Query for the corresponding country
				oldCountryQuery.equalTo("name",oldChannelCountry);
				oldCountryQuery.first().then(function(oldCountry) {
					
					var oldCountryUsers = oldCountry.get("nbrUsers");
					var oldCountryChannels = oldCountry.get("nbrChannels");
					oldCountry.set("nbrUsers",oldCountryUsers-1); // Updating all concerned fields for country and channel
					oldCountry.set("avUsersPerChannel",Math.floor((oldCountryUsers-1)/(oldCountryChannels+1))); // No need to lower the nbrChannels for the country
																												// the channel will be filled again at the next subscription
					oldChannel.set("members",oldChannelMembers-1);
					if (oldChannel.get("full")) { // if the channel was full, it isn't any longer
						oldChannel.set("full",false);
					}
					return Parse.Object.saveAll([oldChannel,oldCountry]); // Saving old channel and old country

				});
				}, function (error){
				console.error("Couldn't update old channel") // There was an error somewhere
			});
		}

		var country = request.object.get("country");	
		
		var countryQuery = new Parse.Query("Country"); 
		countryQuery.equalTo("name",country); 
		countryQuery.first().then(function(resultCountry) { // Querying the first (the only) country with matching name
		
		var nbrUsers = resultCountry.get("nbrUsers");
		var nbrChannels = resultCountry.get("nbrChannels");
			
			if (nbrChannels == -1){ // If no channel has been created for this country
				
				var newChannel = new Parse.Object("Channel"); // Creation of the first channel
				newChannel.set("name",country + "_0");
				newChannel.set("full",false);
				newChannel.set("country",country);
				newChannel.set("members",1);
				newChannel.set("lastTester",0);
				newChannel.set("maxMembers",maxMembers);
				newChannel.set("counted",false); // The channel isn't counted until it's full
				resultCountry.set("nbrChannels",0); // Updating country: nbrChannels = 0 means there is only one 
													// channel, which has never been full. Once it is full for the 
													// first time, nbrChannels will be set to 1 and a new channel 
													// will be created the next time. nbrChannels must always
													// be the number of channels that have already been full, 
													// so as to be sure that they are never empty: it is very
													// unlikely that a once full channel would become empty
				resultCountry.set("nbrUsers",1);
				resultCountry.set("avUsersPerChannel",1);
				request.object.set("channel",country + "_0"); // Updating User		
				request.object.set("changeOfChannel",false);
				return Parse.Object.saveAll([resultCountry,newChannel]); // Saving country and new channel
				
				} else { // If one or more channels have been created
				
				var channelQuery = new Parse.Query("Channel");
				channelQuery.equalTo("country",country);
				channelQuery.notEqualTo("full", true);
				return channelQuery.first().then(function(resultChannel) { // Querying the first matching, unfull, channel
					if (resultChannel != null) {
					var m = resultChannel.get("members");
					var channelName = resultChannel.get("name");
					resultChannel.set("members", m + 1);
					resultChannel.set("maxMembers",maxMembers);
						if (m + 1 >= maxMembers) {
							resultChannel.set("full", true);
							resultCountry.set("nbrUsers",nbrUsers+1);
							if (resultChannel.get("counted") == false){
							resultCountry.set("nbrChannels",nbrChannels + 1);
							resultChannel.set("counted",true);
							} // Updating country: if the channel is now full, check if it has already 
							  // been counted in nbrChannels for this country, if not, add 1 to nbrChannels
						} else {
							resultCountry.set("nbrUsers",nbrUsers+1);
						}
						resultCountry.set("avUsersPerChannel",Math.floor((nbrUsers+1)/(nbrChannels+1)));
					request.object.set("channel",channelName);
					request.object.set("changeOfChannel",false);
					return Parse.Object.saveAll([resultChannel,resultCountry]); // Saving country and channel
					
					} else { // If all the matching channels are full: we need to create a new one
					
					var newChannel = new Parse.Object("Channel"); // Creation of a new channel
					newChannel.set("name",country + "_" + nbrChannels);
					newChannel.set("full",false);
					newChannel.set("country",country);
					newChannel.set("members",1);
					newChannel.set("lastTester",0);
					newChannel.set("maxMembers",maxMembers);
					newChannel.set("counted",false);
					resultCountry.set("nbrUsers",nbrUsers+1);
					resultCountry.set("avUsersPerChannel",Math.floor((nbrUsers+1)/(nbrChannels+1))); // Updating country (no new channel because it has never
															  // been full yet
					request.object.set("channel",country + "_" + nbrChannels);
					request.object.set("changeOfChannel",false);
					return Parse.Object.saveAll([newChannel,resultCountry]); // Saving country and channel
					
					}
				}, function(error) {
					console.error("there was an error" + error);
				});
					
				}
				
		}).then(function(){
		console.log("All good");
		response.success(); // Everything was successful
		},
		function (error){
		console.error("Oops, something went wrong!") // There was an error somewhere
			});
	
	} else { // There is no change of channel, just saving the User
	
	console.log("end");
	response.success();
	}
	
});

// Updating all installations linked to the user being saved
Parse.Cloud.afterSave(Parse.User, function(request) {
	Parse.Cloud.useMasterKey();
	var channel = request.object.get("channel");
	var useLocation = request.object.get("useLocation");
	var locationKnown = request.object.get("locationKnown");
	var international = request.object.get("international");
	var internationalChannel = request.object.get("internationalChannel");
	if (typeof request.object.getACL() === "undefined") {
	var newACL = new Parse.ACL(); // No public read access for users => only the user can read (and write) his own data:
	newACL.setPublicReadAccess(false);
	request.object.setACL(newACL);
	request.object.save();
	} else {
	var query = new Parse.Query(Parse.Installation);
	query.equalTo("user",request.object);
	query.each(function(installation) {
		installation.set("channel",channel); // Updating the channel
		installation.set("useLocation",useLocation); // Updating the location setting
		installation.set("locationKnown",locationKnown); // Updating the location knowledge status
		installation.set("international", international); // Updating the international setting
		installation.set("internationalChannel", internationalChannel); //Updating the international channel
		installation.save();
	});
	}
});

// Calculating the number of users withing a certain radius of the user
Parse.Cloud.define("usersAround", function(request, response) {
  Parse.Cloud.useMasterKey();
  var userGeoPoint = request.user.get("location");
  var radius = request.params.radius;
  var userQuery = new Parse.Query(Parse.User);
  userQuery.equalTo("useLocation", true);
  userQuery.equalTo("locationKnown",true);
  userQuery.withinKilometers("location", userGeoPoint, radius);
  userQuery.count().then(function(number){
	response.success(number);
	}, function(error) {
	console.error(error);
	});
});

// Creating a new question
Parse.Cloud.define("newQuestion", function(request, response) {
  Parse.Cloud.useMasterKey();
  var asker = Parse.User.current();
  var asker_username = asker.get("username");
  var country = asker.get("country");
  var text = request.params.text;
  var nbrChannels = request.params.nbrChannels;
  var nbrAnswers = request.params.nbrAnswers;
  var international = request.params.international;
  var around = request.params.around;
  var radius = request.params.radius; // Getting the parameters from the User in the app

  switch (nbrAnswers) { // 4 cases depending on the number of possible answers to the question
	case 2:
		var newQuestion = new Parse.Object("Question");
		newQuestion.set("asker",asker);
		newQuestion.set("askerUsername",asker_username);
		newQuestion.set("country",country);
		newQuestion.set("text",text);
		newQuestion.set("interested",0);
		newQuestion.set("notInterested",0);
		newQuestion.set("nbrAnswers",nbrAnswers);
		newQuestion.set("answer1",request.params.answer1);
		newQuestion.set("answer2",request.params.answer2);
		newQuestion.set("answer3","");
		newQuestion.set("answer4","");
		newQuestion.set("answer5","");
		newQuestion.set("nA",0);
		newQuestion.set("nA1",0);
		newQuestion.set("nA2",0);
		newQuestion.set("nA3",0);
		newQuestion.set("nA4",0);
		newQuestion.set("nA5",0);
		newQuestion.set("numberOfChannels",nbrChannels);
		newQuestion.set("channels", []);
		newQuestion.set("toSendForTest",true);
		newQuestion.set("published",false);
		newQuestion.set("approvedAndSent",false);
		newQuestion.set("international",international);
		newQuestion.set("around",around);
		newQuestion.set("radius",radius); // Setting the question's fields and saving
		newQuestion.save().then(function(){
		response.success();
		}, function(error){
		console.error("There was a problem");
		});
	break;
	case 3:
		var newQuestion = new Parse.Object("Question");
		newQuestion.set("asker",asker);
		newQuestion.set("askerUsername",asker_username);
		newQuestion.set("country",country);
		newQuestion.set("text",text);
		newQuestion.set("interested",0);
		newQuestion.set("notInterested",0);
		newQuestion.set("nbrAnswers",nbrAnswers);
		newQuestion.set("answer1",request.params.answer1);
		newQuestion.set("answer2",request.params.answer2);
		newQuestion.set("answer3",request.params.answer3);
		newQuestion.set("answer4","");
		newQuestion.set("answer5","");
		newQuestion.set("nA",0);
		newQuestion.set("nA1",0);
		newQuestion.set("nA2",0);
		newQuestion.set("nA3",0);
		newQuestion.set("nA4",0);
		newQuestion.set("nA5",0);
		newQuestion.set("numberOfChannels",nbrChannels);
		newQuestion.set("channels", []);
		newQuestion.set("toSendForTest",true);
		newQuestion.set("published",false);
		newQuestion.set("approvedAndSent",false);
		newQuestion.set("international",international);
		newQuestion.set("around",around);
		newQuestion.set("radius",radius); // Setting the question's fields and saving
		newQuestion.save().then(function(){
		response.success();
		}, function(error){
		console.error("There was a problem");
		});
	break;
	case 4:
		var newQuestion = new Parse.Object("Question");
		newQuestion.set("asker",asker);
		newQuestion.set("askerUsername",asker_username);
		newQuestion.set("country",country);
		newQuestion.set("text",text);
		newQuestion.set("interested",0);
		newQuestion.set("notInterested",0);
		newQuestion.set("nbrAnswers",nbrAnswers);
		newQuestion.set("answer1",request.params.answer1);
		newQuestion.set("answer2",request.params.answer2);
		newQuestion.set("answer3",request.params.answer3);
		newQuestion.set("answer4",request.params.answer4);
		newQuestion.set("answer5","");
		newQuestion.set("nA",0);
		newQuestion.set("nA1",0);
		newQuestion.set("nA2",0);
		newQuestion.set("nA3",0);
		newQuestion.set("nA4",0);
		newQuestion.set("nA5",0);
		newQuestion.set("numberOfChannels",nbrChannels);
		newQuestion.set("channels", []);
		newQuestion.set("toSendForTest",true);
		newQuestion.set("published",false);
		newQuestion.set("approvedAndSent",false);
		newQuestion.set("international",international);
		newQuestion.set("around",around);
		newQuestion.set("radius",radius); // Setting the question's fields and saving
		newQuestion.save().then(function(){
		response.success();
		}, function(error){
		console.error("There was a problem");
		});
	break;
	case 5:
		var newQuestion = new Parse.Object("Question");
		newQuestion.set("asker",asker);
		newQuestion.set("askerUsername",asker_username);
		newQuestion.set("country",country);
		newQuestion.set("text",text);
		newQuestion.set("interested",0);
		newQuestion.set("notInterested",0);
		newQuestion.set("nbrAnswers",nbrAnswers);
		newQuestion.set("answer1",request.params.answer1);
		newQuestion.set("answer2",request.params.answer2);
		newQuestion.set("answer3",request.params.answer3);
		newQuestion.set("answer4",request.params.answer4);
		newQuestion.set("answer5",request.params.answer5);
		newQuestion.set("nA",0);
		newQuestion.set("nA1",0);
		newQuestion.set("nA2",0);
		newQuestion.set("nA3",0);
		newQuestion.set("nA4",0);
		newQuestion.set("nA5",0);
		newQuestion.set("numberOfChannels",nbrChannels);
		newQuestion.set("channels", []);
		newQuestion.set("toSendForTest",true);
		newQuestion.set("published",false);
		newQuestion.set("approvedAndSent",false);
		newQuestion.set("international",international);
		newQuestion.set("around",around);
		newQuestion.set("radius",radius); // Setting the question's fields and saving
		newQuestion.save().then(function(){
		response.success();
		}, function(error){
		console.error("There was a problem");
		});
	break;
		response.error("Unable to submit question");
  }
});

Parse.Cloud.define("addInterest", function(request, response) {
  Parse.Cloud.useMasterKey();
  var query = new Parse.Query("Question");
  query.get(request.params.id, {
	success: function(question) {
		if (request.params.interest == 1){
		var interested = question.get("interested");
		question.set("interested",interested+1);
		question.save().then(function() {
		response.success();
		}, function(error) {
		response.error("Unable to register interest");
		});
		} else {
		var notInterested = question.get("notInterested");
		question.set("notInterested",notInterested+1);
		question.save().then(function() {
		response.success();
		}, function(error) {
		response.error("Unable to register lack of interest");
		});
		}
	},
	error: function() {
      response.error("Unable to register interest");
    }
	});
});

Parse.Cloud.define("addAnswer", function(request, response) {
  Parse.Cloud.useMasterKey();
  var n = request.params.n;
  var query = new Parse.Query("Question");
  query.get(request.params.id, {
	success: function(question) {
		switch (n){
			case 1:
				var nA = question.get("nA");
				question.set("nA",nA+1);
				var nA1 = question.get("nA1");
				question.set("nA1",nA1+1);
				question.save().then(function(){
				response.success();
				}, function(error){
				console.error("There was a problem");
				});
			break;
			case 2:
				var nA = question.get("nA");
				question.set("nA",nA+1);
				var nA2 = question.get("nA2");
				question.set("nA2",nA2+1);
				question.save().then(function(){
				response.success();
				}, function(error){
				console.error("There was a problem");
				});				
			break;
			case 3:
				var nA = question.get("nA");
				question.set("nA",nA+1);
				var nA3 = question.get("nA3");
				question.set("nA3",nA3+1);
				question.save().then(function(){
				response.success();
				}, function(error){
				console.error("There was a problem");
				});				
			break;
			case 4:
				var nA = question.get("nA");
				question.set("nA",nA+1);
				var nA4 = question.get("nA4");
				question.set("nA4",nA4+1);
				question.save().then(function(){
				response.success();
				}, function(error){
				console.error("There was a problem");
				});
			break;
			case 5:
				var nA = question.get("nA");
				question.set("nA",nA+1);
				var nA5 = question.get("nA5");
				question.set("nA5",nA5+1);
				question.save().then(function(){
				response.success();
				}, function(error){
				console.error("There was a problem");
				});
			break;
			default: 
				response.error("Unable to register answer");
		}
	},
	error: function() {
      response.error("Unable to register answer");
    }
	});
});

Parse.Cloud.beforeSave("Question", function(request,response){
	Parse.Cloud.useMasterKey();
	var n = request.object.get("nbrAnswers");
	var nI = request.object.get("interested");
	var nA = request.object.get("nA");
	var approvedAndSent = request.object.get("approvedAndSent");
	var published = request.object.get("published");
	var minToA = 1;
	var minForR = 1;
	request.object.set("minToBeApproved",minToA);
	request.object.set("minForResults",minForR);
	if (nI == minToA && !approvedAndSent) {
		request.object.set("toSendForAnswer",true);
		} else {
		request.object.set("toSendForAnswer",false);
		}
	if (nA == minForR && !published) {
		request.object.set("toSendForResults",true);
		} else {
		request.object.set("toSendForResults",false);
		}
		
	if (nA == 0){
	request.object.set("pcA1",0);
	request.object.set("pcA2",0);
	request.object.set("pcA3",0);
	request.object.set("pcA4",0);
	request.object.set("pcA5",0);
	} else {
	var nA1 = request.object.get("nA1");
	var nA2 = request.object.get("nA2");
	var nA3 = request.object.get("nA3");
	var nA4 = request.object.get("nA4");
	var nA5 = request.object.get("nA5");
	request.object.set("pcA1",(nA1*100/nA));
	request.object.set("pcA2",(nA2*100/nA));
	request.object.set("pcA3",(nA3*100/nA));
	request.object.set("pcA4",(nA4*100/nA));
	request.object.set("pcA5",(nA5*100/nA));
	}
	
	response.success();
	});

Parse.Cloud.afterSave("Question", function(request){
	Parse.Cloud.useMasterKey();
	var time_for_interest = 86400; // expiration time for notification requesting approval
	var time_for_answer = 86400; // expiration time for notification requesting answer
	var time_for_results = 86400; // expiration time for notification giving results
	
	var international = request.object.get("international");
	if (international) {
	var country = "international";
	} else {
	var country = request.object.get("country");
	}
	var around = request.object.get("around");
	var toSendForTest = request.object.get("toSendForTest");
	var toSendForAnswer = request.object.get("toSendForAnswer");
	var toSendForResults = request.object.get("toSendForResults");
		
	if (toSendForTest || toSendForAnswer || toSendForResults) {
		
	if (toSendForTest) {	
	
	if (around) {
	
	var userGeoPoint = request.user.get("location");
	var radius = request.object.get("radius");
	var pushQuery = new Parse.Query(Parse.Installation);
	pushQuery.equalTo("useLocation", true);
	pushQuery.equalTo("locationKnown",true);
	pushQuery.withinKilometers("location", userGeoPoint, radius);
	pushQuery.find().then(function(results) {
		for (i=0;i<results.length;i++) {
			request.object.add("followers",results[i].id);
		}
		pushQuery.limit(10);
		return Parse.Push.send({
		where: pushQuery,
		data: {
			action: "com.spersio.opinion.ANSWER_INTEREST",
			//alert: "Question: " + request.object.get("text"),
			//title: "Your approval is needed!",
			questionID: request.object.id,
			questionText: request.object.get("text"),
			nbrAnswers: request.object.get("nbrAnswers"),
			answer1: request.object.get("answer1"),
			answer2: request.object.get("answer2"),
			answer3: request.object.get("answer3"),
			answer4: request.object.get("answer4"),
			answer5: request.object.get("answer5"),
			international: request.object.get("international"),
			around: request.object.get("around"),
			radius: request.object.get("radius"),
			askerUsername: request.object.get("askerUsername")
		},
		expiration_interval: time_for_interest		
		}, {
		success: function(){
			request.object.set("toSendForTest",false);
			request.object.save();
			},
		error: function(error) {}	
			});
	}, function (error) {
		console.error("error");
		}
	);
	
	
	} else {
	
	var numberOfChannels = request.object.get("numberOfChannels");
	var countryQuery = new Parse.Query("Country");
	var nbrChannel = 0;
	countryQuery.equalTo("name",country);
	countryQuery.first({
	success: function(result) {
	var nbrChannels = result.get("nbrChannels");
	var alreadyUsedChannels = new Array(nbrChannels);
		for (i=0;i<nbrChannels;i++){
		alreadyUsedChannels[i]=0;
		}
	
	for (j=0;j<numberOfChannels;j++){
	
	do {
	nbrChannel = Math.floor(Math.random() * nbrChannels);
	console.log("nbrChannel" + nbrChannel);
	console.log("already Used:" + alreadyUsedChannels[nbrChannel]);
	}
	while (alreadyUsedChannels[nbrChannel]==1);
	console.log("sortie de boucle");
	var channelQuery = new Parse.Query("Channel");
	channelQuery.equalTo("name",country + '_' + nbrChannel);
	channelQuery.first({
	success: function(resultC) {
	//var nbrMembers = resultC.get("members");
	//var start = Math.floor((Math.random() * (nbrMembers)) + 1);
	
	var pushQuery = new Parse.Query(Parse.Installation);
	if (international) {
	pushQuery.equalTo("internationalChannel", 'international_' + nbrChannel);
	} else {
	pushQuery.equalTo("channel",country + '_' + nbrChannel);
	}
	//pushQuery.skip(start-1);
	//pushQuery.limit(10);
	Parse.Push.send({
		where: pushQuery,
		data: {
			action: "com.spersio.opinion.ANSWER_INTEREST",
			//alert: "Question: " + request.object.get("text"),
			//title: "Your approval is needed!",
			questionID: request.object.id,
			questionText: request.object.get("text"),
			nbrAnswers: request.object.get("nbrAnswers"),
			answer1: request.object.get("answer1"),
			answer2: request.object.get("answer2"),
			answer3: request.object.get("answer3"),
			answer4: request.object.get("answer4"),
			answer5: request.object.get("answer5"),
			international: request.object.get("international"),
			around: request.object.get("around"),
			radius: request.object.get("radius"),
			askerUsername: request.object.get("askerUsername")
		},
		expiration_interval: time_for_interest		
		}, {
	success: function(){
		alreadyUsedChannels[nbrChannel]=1;
		request.object.set("toSendForTest",false);
		request.object.add("channels",country + '_' + nbrChannel);
		request.object.save();
		},
	error: function(error) {}	
		});
	},
 	error: function(error) {
	alert("Error: " + error.code + " " + error.message);
		}});
	}
	},
	error: function(error) {
    alert("Error: " + error.code + " " + error.message);
	}
	});
	}
	}
	
	if (toSendForAnswer) {
	
	if (around) {
	

	var asker = request.object.get("asker");

	var pushQuery = new Parse.Query(Parse.Installation);
	pushQuery.containedIn("objectId",request.object.get("followers"));
	Parse.Push.send({
		where: pushQuery,
		data: {
			action: "com.spersio.opinion.ANSWER_QUESTION",
			//alert: "Question: " + request.object.get("text"),
			//title : "Your opinion is needed!",
			questionID: request.object.id,
			questionText: request.object.get("text"),
			nbrAnswers: request.object.get("nbrAnswers"),
			answer1: request.object.get("answer1"),
			answer2: request.object.get("answer2"),
			answer3: request.object.get("answer3"),
			answer4: request.object.get("answer4"),
			answer5: request.object.get("answer5"),
			international: request.object.get("international"),
			around: request.object.get("around"),
			radius: request.object.get("radius"),
			askerUsername: request.object.get("askerUsername")
		},
		expiration_interval: time_for_answer		
		}, {
		success: function(){
			request.object.set("toSendForAnswer",false);
			request.object.set("approvedAndSent",true);
			request.object.save();
			},
		error: function(error) {}	
			});
	var pushAskerQuery = new Parse.Query(Parse.Installation);
	pushAskerQuery.equalTo("user",asker);
	Parse.Push.send({
		where: pushAskerQuery,
		data: {
			alert: "Question: " + request.object.get("text"),
			title: "Your question is interesting to people!"
		},
		expiration_interval: time_for_answer
		}, {
		success: function(){
			},
		error: function(error) {}	
			});
	
	} else {
	
	var channels = request.object.get("channels");
	var asker = request.object.get("asker");
	for (i=0 ;i < channels.length; i++) {
	var pushQuery = new Parse.Query(Parse.Installation);
	if (international) {
	pushQuery.equalTo("internationalChannel",channels[i]);
	} else {
	pushQuery.equalTo("channel",channels[i]);
	}
	Parse.Push.send({
		where: pushQuery,
		data: {
			action: "com.spersio.opinion.ANSWER_QUESTION",
			//alert: "Question: " + request.object.get("text"),
			//title : "Your opinion is needed!",
			questionID: request.object.id,
			questionText: request.object.get("text"),
			nbrAnswers: request.object.get("nbrAnswers"),
			answer1: request.object.get("answer1"),
			answer2: request.object.get("answer2"),
			answer3: request.object.get("answer3"),
			answer4: request.object.get("answer4"),
			answer5: request.object.get("answer5"),
			international: request.object.get("international"),
			around: request.object.get("around"),
			radius: request.object.get("radius"),
			askerUsername: request.object.get("askerUsername")
		},
		expiration_interval: time_for_answer	
		}, {
		success: function(){
		request.object.set("toSendForAnswer",false);
		request.object.set("approvedAndSent",true);
		request.object.save();
		},
	error: function(error) {}	
		});
	}
	var pushAskerQuery = new Parse.Query(Parse.Installation);
		pushAskerQuery.equalTo("user",asker);
		Parse.Push.send({
		where: pushAskerQuery,
		data: {
			alert: "Question: " + request.object.get("text"),
			title: "Your question is interesting to people!"
			},
		expiration_interval: time_for_answer
		}, {
		success: function(){
		},
		error: function(error) {}	
		});
	}
	}	
		
	if (toSendForResults) {
	
	if (around) {
	
	var asker = request.object.get("asker");

	var pushQuery = new Parse.Query(Parse.Installation);
	pushQuery.containedIn("objectId",request.object.get("followers"));
	Parse.Push.send({
		where: pushQuery,
		data: {
			action: "com.spersio.opinion.QUESTION_RESULTS",
			//alert: "Question: " + request.object.get("text"),
			//title: "The results are in!",
			questionID: request.object.id,
			nbrAnswers: request.object.get("nbrAnswers"),
			nA: request.object.get("nA"),
			nA1: request.object.get("nA1"),
			nA2: request.object.get("nA2"),
			nA3: request.object.get("nA3"),
			nA4: request.object.get("nA4"),
			nA5: request.object.get("nA5"),
			pcA1: request.object.get("pcA1"),
			pcA2: request.object.get("pcA2"),
			pcA3: request.object.get("pcA3"),
			pcA4: request.object.get("pcA4"),
			pcA5: request.object.get("pcA5"),
			answer1: request.object.get("answer1"),
			answer2: request.object.get("answer2"),
			answer3: request.object.get("answer3"),
			answer4: request.object.get("answer4"),
			answer5: request.object.get("answer5"),
			international: request.object.get("international"),
			around: request.object.get("around"),
			radius: request.object.get("radius"),
			askerUsername: request.object.get("askerUsername")
		},
		expiration_interval: time_for_results		
		}, {
		success: function(){
			request.object.set("toSendForResults",false);
			request.object.set("published",true);
			request.object.save();
			},
		error: function(error) {}	
			});
	var pushAskerQuery = new Parse.Query(Parse.Installation);
	pushAskerQuery.equalTo("user",asker);
	Parse.Push.send({
		where: pushAskerQuery,
		data: {
			action: "com.spersio.opinion.QUESTION_RESULTS",
			//alert: "Question: " + request.object.get("text"),
			//title: "The results are in!",
			questionID: request.object.id,
			questionText: request.object.get("text"),
			nbrAnswers: request.object.get("nbrAnswers"),
			nA: request.object.get("nA"),
			nA1: request.object.get("nA1"),
			nA2: request.object.get("nA2"),
			nA3: request.object.get("nA3"),
			nA4: request.object.get("nA4"),
			nA5: request.object.get("nA5"),
			pcA1: request.object.get("pcA1"),
			pcA2: request.object.get("pcA2"),
			pcA3: request.object.get("pcA3"),
			pcA4: request.object.get("pcA4"),
			pcA5: request.object.get("pcA5"),
			answer1: request.object.get("answer1"),
			answer2: request.object.get("answer2"),
			answer3: request.object.get("answer3"),
			answer4: request.object.get("answer4"),
			answer5: request.object.get("answer5"),
			international: request.object.get("international"),
			around: request.object.get("around"),
			radius: request.object.get("radius"),
			askerUsername: request.object.get("askerUsername")
		},
		expiration_interval: time_for_results
		}, {
		success: function(){
			},
		error: function(error) {}	
			});
	
	} else {
	
	var channels = request.object.get("channels");
	var asker = request.object.get("asker");
	for (i=0 ;i < channels.length; i++) {		
	var pushQuery = new Parse.Query(Parse.Installation);
	if (international) {
	pushQuery.equalTo("internationalChannel",channels[i]);
	} else {
	pushQuery.equalTo("channel",channels[i]);
	}
	Parse.Push.send({
		where: pushQuery,
		data: {
			action: "com.spersio.opinion.QUESTION_RESULTS",
			//alert: "Question: " + request.object.get("text"),
			//title: "The results are in!",
			questionID: request.object.id,
			questionText: request.object.get("text"),			
			nbrAnswers: request.object.get("nbrAnswers"),
			nA: request.object.get("nA"),
			nA1: request.object.get("nA1"),
			nA2: request.object.get("nA2"),
			nA3: request.object.get("nA3"),
			nA4: request.object.get("nA4"),
			nA5: request.object.get("nA5"),
			pcA1: request.object.get("pcA1"),
			pcA2: request.object.get("pcA2"),
			pcA3: request.object.get("pcA3"),
			pcA4: request.object.get("pcA4"),
			pcA5: request.object.get("pcA5"),
			answer1: request.object.get("answer1"),
			answer2: request.object.get("answer2"),
			answer3: request.object.get("answer3"),
			answer4: request.object.get("answer4"),
			answer5: request.object.get("answer5"),
			international: request.object.get("international"),
			around: request.object.get("around"),
			radius: request.object.get("radius"),
			askerUsername: request.object.get("askerUsername")
		},
		expiration_interval: time_for_results
		}, {
	success: function(){
		request.object.set("toSendForResults",false);
		request.object.set("published",true);
		request.object.save();
		},
	error: function(error) {}	
		});
	}
	var pushAskerQuery = new Parse.Query(Parse.Installation);
	pushAskerQuery.equalTo("user",asker);
	Parse.Push.send({
		where: pushAskerQuery,
		data: {
			action: "com.spersio.opinion.QUESTION_RESULTS",
			//alert: "Question: " + request.object.get("text"),
			//title: "The results are in!",
			questionID: request.object.id,
			nbrAnswers: request.object.get("nbrAnswers"),
			nA: request.object.get("nA"),
			nA1: request.object.get("nA1"),
			nA2: request.object.get("nA2"),
			nA3: request.object.get("nA3"),
			nA4: request.object.get("nA4"),
			nA5: request.object.get("nA5"),
			pcA1: request.object.get("pcA1"),
			pcA2: request.object.get("pcA2"),
			pcA3: request.object.get("pcA3"),
			pcA4: request.object.get("pcA4"),
			pcA5: request.object.get("pcA5"),
			answer1: request.object.get("answer1"),
			answer2: request.object.get("answer2"),
			answer3: request.object.get("answer3"),
			answer4: request.object.get("answer4"),
			answer5: request.object.get("answer5"),
			international: request.object.get("international"),
			around: request.object.get("around"),
			radius: request.object.get("radius"),
			askerUsername: request.object.get("askerUsername")
		},
		expiration_interval: time_for_results
		}, {
	success: function(){
		},
	error: function(error) {}	
		});
	}
	}
	}
});

Parse.Cloud.job("channelsMembersCount", function(request, status) {
	Parse.Cloud.useMasterKey();
    var channelQuery = new Parse.Query("Channel");
	channelQuery.each(function(channel) {
		var channelName = channel.get("name");
		
		var query = new Parse.Query(Parse.User);
		query.equalTo("channel", channelName);
		return query.count().then(function(number){
			channel.set("members",number);
			channel.set("full", (number==channel.get("maxMembers")));
			return channel.save();
			}, function(error){
			console.error("error counting:" + error);
		});
	
		}).then(function() {
		status.success("Members count successful.");
		}, function(error) {
		status.error("Oops, something went wrong!");
		});
});

Parse.Cloud.job("emptyChannelsDelete", function(request, status) {
	Parse.Cloud.useMasterKey();
    var channelQuery = new Parse.Query("Channel");
	channelQuery.each(function(channel) {
		var members = channel.get("members");
		if (members == 0) {
			return channel.destroy({
			success: function(myObject) {
			// The object was deleted from the Parse Cloud.
			},
			error: function(myObject, error) {
		// The delete failed.
		// error is a Parse.Error with an error code and description.
			}
			});
		}
	
		}).then(function() {
		status.success("Empty channels deletion successful.");
		}, function(error) {
		status.error("Oops, something went wrong!");
		});
});

Parse.Cloud.job("internationalUsersCount", function(request, status) {
	Parse.Cloud.useMasterKey();
	
	var query = new Parse.Query(Parse.User);
	query.equalTo("international", true);
	return query.count().then(function(number){
		
		var query = new Parse.Query("Country");
		query.get("7wQb7cZnQH", {
		  success: function(country) {
		  country.set("nbrUsers",number);
		  country.set("avUsersPerChannel",Math.floor(number/(country.get("nbrChannels")+1)));
		  return country.save();
		  console.log("There is " + number + " international users");
		  },
		  error: function(object, error) {
		  }
		});
		
		}, function(error){
		console.error("error counting:" + error);
	});
});

Parse.Cloud.job("internationalChannelsCount", function(request, status) {
	Parse.Cloud.useMasterKey();
	
	var query = new Parse.Query("Channel");
	query.equalTo("country", international);
	query.equalTo("counted", true);
	return query.count().then(function(number){
		
		var query = new Parse.Query("Country");
		query.get("7wQb7cZnQH", {
		  success: function(country) {
		  country.set("nbrChannels",number-1);
		  return country.save();
		  console.log("There is " + number + " international channels");
		  },
		  error: function(object, error) {
		  }
		});
		
		}, function(error){
		console.error("error counting:" + error);
	});
});

Parse.Cloud.job("setChannelsMaxMembers", function(request, status) {
	Parse.Cloud.useMasterKey();
	var maxMembers = 3;
    var channelQuery = new Parse.Query("Channel");
	channelQuery.each(function(channel) {
		channel.set("maxMembers",maxMembers);
		return channel.save();
		}).then(function() {
		status.success("Max number of Users per channel set to " + maxMembers);
		}, function(error) {
		status.error("Oops, something went wrong!");
		});
});

Parse.Cloud.job("channelsPerCountry", function(request, status) {
	Parse.Cloud.useMasterKey();
    var countryQuery = new Parse.Query("Country");
	countryQuery.notEqualTo("nbrChannels",-1);
	countryQuery.notEqualTo("name","international");
	countryQuery.each(function(country) {
		var countryName = country.get("name");
		
		var channel = Parse.Object.extend("Channel");
		var channelQuery = new Parse.Query(channel);
		channelQuery.equalTo("country", countryName);
		channelQuery.equalTo("counted", true);
		return channelQuery.count().then(function(number){
			country.set("nbrChannels",number-1);
			return country.save();
			});
		
		}).then(function() {
		status.success("Channels per country successfully counted.");
		}, function(error) {
		status.error("Oops, something went wrong!");
		});
});
	
Parse.Cloud.job("usersPerCountry", function(request, status) {
	Parse.Cloud.useMasterKey();
    var countryQuery = new Parse.Query("Country");
	countryQuery.notEqualTo("nbrChannels",-1);
	countryQuery.each(function(country) {
		var countryName = country.get("name");
		
		var userQuery = new Parse.Query(Parse.User);
		userQuery.equalTo("country", countryName);
		return userQuery.count().then(function(numberU){
			country.set("nbrUsers",numberU);
			country.set("avUsersPerChannel",Math.floor(numberU/(country.get("nbrChannels")+1)));
			return(country.save());
		});
	
		}).then(function() {
		status.success("Users per country successfully counted.");
		}, function(error) {
		status.error("Oops, something went wrong!");
		});
});

// The job usersLocation sets locationKnown to false for all users who haven't been updated in more than a certain time.
// The related installations are automatically updated by the afterSave function.
Parse.Cloud.job("usersLocation", function(request, status) {
	Parse.Cloud.useMasterKey();
    var userQuery = new Parse.Query(Parse.User);
	userQuery.equalTo("locationKnown",true);
	userQuery.each(function(user) {
		
		var update = user.updatedAt;
		var now = new Date();
		var updatedAt = new Date(update);
		var time_diff = now.getTime() - updatedAt.getTime();
	
		if (time_diff > 10000) {
		user.set("locationKnown",false);
		return user.save();
		}
	
		}).then(function() {
		status.success("Users known location successfully updated.");
		}, function(error) {
		status.error("Oops, something went wrong!");
		});
});


Parse.Cloud.job("questionsDelete", function(request, status) {
	Parse.Cloud.useMasterKey();
	var time_for_approval = 30000;
	var time_for_results = 86400000;
    var questionQuery = new Parse.Query("Question");
	questionQuery.each(function(question) {
		var creation = question.createdAt;
		var published = question.get("published");
		var approvedAndSent = question.get("approvedAndSent");
		var asker = question.get("asker");
		var now = new Date();
		var createdAt = new Date(creation);
		
		var time_diff = now.getTime() - createdAt.getTime();

		if (!approvedAndSent && (time_diff > time_for_approval)) {
			var pushAskerQuery = new Parse.Query(Parse.Installation);
			pushAskerQuery.equalTo("user",asker);
			Parse.Push.send({
			where: pushAskerQuery,
			data: {
				//alert: "Question: " + question.get("text"),
				title: "Question deleted (not enough interest)"
			},
			expiration_interval: 86400
			}, {
			success: function(){
			},
			error: function(error) {}	
			});
			question.destroy({
			success: function(myObject) {	
			},
			error: function(myObject, error) {
		// The delete failed.
		// error is a Parse.Error with an error code and description.
			}
			});
			
			}
		
		if (!published && (time_diff > time_for_results)) {
			var pushAskerQuery = new Parse.Query(Parse.Installation);
			pushAskerQuery.equalTo("user",asker);
			return Parse.Push.send({
			where: pushAskerQuery,
			data: {
				//alert: "Question: " + question.get("text"),
				title: "Question deleted (not enough answers)"
			},
			expiration_interval: 86400
			}, {
			success: function(){
			},
			error: function(error) {}	
			});
			question.destroy({
			success: function(myObject) {
			},
			error: function(myObject, error) {
		// The delete failed.
		// error is a Parse.Error with an error code and description.
			}
			});	
		}
	
		}).then(function() {
		status.success("Questions deletion successful.");
		}, function(error) {
		status.error("Oops, something went wrong!");
		});
});