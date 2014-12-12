require("cloud/app.js");

//////// Cloud Code for Opinion ////////
////// I - Users: beforeSave, afterSave, usersAround, addSubscriber, subtractSubscriber, usersLocation, savedQuestions, deleteSavedQuestions and userExists
////// II - Questions: newQuestion, beforeSave, afterSave, addapproval, addAnswer, questionsDeleteForLackOfApproval and questionsDeleteForLackOfAnswers
////// III - Channels: checkFullChannels
////// IV - Countries: getCountry


////// I - Users: beforeSave, afterSave, usersAround, addSubscriber, subtractSubscriber, usersLocation, deleteSavedQuestions and userExists

//// User beforeSave

// Changing the channel of the user if user.save() called from changeOfCountry class (changeOfChannel = true) or if International is turned on for the first time
Parse.Cloud.beforeSave(Parse.User, function(request,response){
	Parse.Cloud.useMasterKey();
	var changeOfChannel = request.object.get("changeOfChannel");
	var international = request.object.get("international");
	var maxMembers = 3; // Setting the maximum number of members per channel

	var now = new Date(); // Current date
	request.object.set("lastUpdate",now.getTime()); // Setting the date of the user's lastUpdate

	if (international && request.object.get("internationalChannel") == "") { // International option is turned on for the first time
		
		var query = new Parse.Query("Country");
		query.equalTo("name", "International");
		query.first().then(function(resultCountry) {
			
			var nbrUsers = resultCountry.get("nbrUsers");
			var nbrChannels = resultCountry.get("nbrChannels");

			if (nbrChannels == -1){ // If no channel has been created for this country
				
				var newChannel = new Parse.Object("Channel"); // Creation of the first channel
				newChannel.set("name","International_" + "0");
				newChannel.set("full",false);
				newChannel.set("country","International");
				newChannel.set("members",1);
				newChannel.set("lastTester",0);
				newChannel.set("maxMembers",maxMembers);
				newChannel.set("counted",false); // The channel isn't counted until it's full
				resultCountry.set("nbrChannels",0); // Updating International: nbrChannels = 0 means there is only one 
													// channel, which has never been full. Once it is full for the 
													// first time, nbrChannels will be set to 1 and a new channel 
													// will be created the next time. nbrChannels must always
													// be the number of channels that have already been full, 
													// so as to be sure that they are never empty: it is very
													// unlikely that a once full channel would become empty
				resultCountry.set("nbrUsers",1);
				resultCountry.set("avUsersPerChannel",1);
				request.object.set("internationalChannel","International_"+ "0"); // Updating User
				return Parse.Object.saveAll([resultCountry,newChannel]); // Saving country and new channel

			} else {	

			var channelQuery = new Parse.Query("Channel");
				channelQuery.equalTo("country","International");
				channelQuery.notEqualTo("full", true);
				return channelQuery.first().then(function(resultChannel) { // Querying the first matching, unfull, channel
					
					if (resultChannel != null) { // If there is one
					var m = resultChannel.get("members");
					var channelName = resultChannel.get("name");
					resultChannel.increment("members"); // Updating the number of members
					resultChannel.set("maxMembers",maxMembers); // Resetting, or updating the maximum number of members. Makes changing the number easier.
						if (m + 1 == maxMembers) {
							resultChannel.set("full", true); // If the maximum number of members is reached, full = true
							resultCountry.increment("nbrUsers"); // Updating the number of users for the "country" International
							if (resultChannel.get("counted") == false){ // If it's the first time the channel gets full
							resultCountry.increment("nbrChannels"); // One more channel for International
							resultChannel.set("counted",true); // The channel is now counted (it has been full at least once)
							resultCountry.set("avUsersPerChannel",Math.min(maxMembers,Math.round((nbrUsers+1)/(nbrChannels+1)))); // Updating "country" International's average number of users per channel
							} else {
								if (nbrChannels == 0) {
								resultCountry.set("avUsersPerChannel",Math.min(maxMembers,nbrUsers+1)); // Updating "country" International's average number of users per channel
								} else {
								resultCountry.set("avUsersPerChannel",Math.min(maxMembers,Math.round((nbrUsers+1)/nbrChannels))); // Updating "country" International's average number of users per channel
								}
							}	
						} else {
							resultCountry.increment("nbrUsers"); // If the maximum numbers is not reached, only the number of users for International is updated
							if (nbrChannels == 0) {
							resultCountry.set("avUsersPerChannel",Math.min(maxMembers,nbrUsers+1));
							} else {
							resultCountry.set("avUsersPerChannel",Math.min(maxMembers,Math.round((nbrUsers+1)/nbrChannels)));
							}
						}
					request.object.set("internationalChannel",channelName); // Setting the user's international channel
					return Parse.Object.saveAll([resultChannel,resultCountry]); // Saving "country" international and channel
					
					} else { // If all the matching channels are full: we need to create a new one
					
					var newChannel = new Parse.Object("Channel"); // Creation of a new channel
					newChannel.set("name", "International_" + nbrChannels); // Naming it
					newChannel.set("full",false); // Not full
					newChannel.set("country","International"); // International Channel
					newChannel.set("members",1); // Only one user
					newChannel.set("lastTester",0); // USELESS?
					newChannel.set("maxMembers",maxMembers); // Setting the maximum number of members
					newChannel.set("counted",false); // Has never been full, so not counted yet
					resultCountry.increment("nbrUsers"); // Updating "country" International's number of users
					resultCountry.set("avUsersPerChannel",Math.min(maxMembers,Math.round((nbrUsers+1)/nbrChannels))); // Updating average number of users per channel
																									 				   // (no new channel because not counted)
					request.object.set("internationalChannel","International_" + nbrChannels); // Setting the user's international channel
					return Parse.Object.saveAll([newChannel,resultCountry]); // Saving "country" international and channel
					
					}
				});
			}

		  	}).then(function(){
				response.success(); // Everything was successful
			}, function (error){
				console.error("Error while setting or changing internationalChannel for user " + request.object.get("username") + " Error : " + error.code + " " + error.message); // If there is an error, it is logged;
			});

	} else {

	if (!international && request.object.get("internationalChannel") != "") { //International option has just been turned off 

		var oldInternationalChannelName = request.object.get("internationalChannel"); // We need to update the number of members of the "country" International and of the user's former international channel

			var oldInternationalChannelQuery = new Parse.Query("Channel"); // Query for the old international channel
			oldInternationalChannelQuery.equalTo("name",oldInternationalChannelName);
			oldInternationalChannelQuery.first().then(function(oldInternationalChannel) {

				var oldInternationalChannelMembers = oldInternationalChannel.get("members");

				var query = new Parse.Query("Country");
				query.equalTo("name", "International");
				return query.first().then(function(resultCountry) {

					var internationalUsers = resultCountry.get("nbrUsers");
					var internationalChannels = resultCountry.get("nbrChannels");

					resultCountry.increment("nbrUsers",-1); // Updating all concerned fields for International and channel
					if (internationalChannels==0) {
					resultCountry.set("avUsersPerChannel",internationalUsers-1);
					} else {
					resultCountry.set("avUsersPerChannel",Math.min(maxMembers,Math.round((internationalUsers-1)/internationalChannels))); // No need to lower the nbrChannels for International
																												// the channel will be filled again at the next subscription
					}
					oldInternationalChannel.increment("members",-1);
					oldInternationalChannel.set("maxMembers",maxMembers); // Resetting, or updating the maximum number of members. Makes changing the number easier.
					if (oldInternationalChannel.get("full") && (oldInternationalChannelMembers-1 < maxMembers)) { // if the channel was full, it isn't any longer, unless maxMembers has changed
						oldInternationalChannel.set("full",false);
					}
					request.object.set("internationalChannel", "");
					return Parse.Object.saveAll([oldInternationalChannel,resultCountry]); // Saving old channel and International

				});
			}).then(function(){
				response.success(); // Everything was successful
			},function (error){
				console.error("Error while updating oldInternationalChannel for user " + request.object.get("username") + " Error : " + error.code + " " + error.message); // If there is an error, it is logged;
			});

	} else {
	
	if (changeOfChannel) { // There is a change of channel
		
		var oldChannelName = request.object.get("customChannel"); // We need to update the number of members of the user's former channel and country

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
					oldCountry.increment("nbrUsers",-1); // Updating all concerned fields for country and channel
					if (oldCountryChannels==0) {
					oldCountry.set("avUsersPerChannel",oldCountryUsers-1);
					} else {
					oldCountry.set("avUsersPerChannel",Math.min(maxMembers,Math.round((oldCountryUsers-1)/oldCountryChannels))); // No need to lower the nbrChannels for the country
																												// the channel will be filled again at the next subscription
					}
					oldChannel.increment("members",-1);
					oldChannel.set("maxMembers",maxMembers); // Resetting, or updating the maximum number of members. Makes changing the number easier.
					if (oldChannel.get("full") && (oldChannelMembers-1 < maxMembers)) { // if the channel was full, it isn't any longer, unless a new maxMembers has been set.
						oldChannel.set("full",false);
					}
					return Parse.Object.saveAll([oldChannel,oldCountry]); // Saving old channel and old country

				});
				}, function (error){
				console.error("Error while updating oldChannel for user " + request.object.get("username") + ". Error : " + error.code + " " + error.message); // If there is an error, it is logged;
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
				request.object.set("customChannel",country + "_0"); // Updating User		
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
					resultChannel.increment("members");
					resultChannel.set("maxMembers",maxMembers); // Resetting, or updating the maximum number of members. Makes changing the number easier.
						if (m + 1 >= maxMembers) {
							resultChannel.set("full", true);
							resultCountry.increment("nbrUsers");
							if (resultChannel.get("counted") == false){
							resultCountry.increment("nbrChannels");
							resultChannel.set("counted",true);
							resultCountry.set("avUsersPerChannel",Math.min(maxMembers,Math.round((nbrUsers+1)/(nbrChannels+1))));
							  // Updating country: if the channel is now full, check if it has already 
							  // been counted in nbrChannels for this country, if not, add 1 to nbrChannels
							} else {
								if (nbrChannels == 0) {
									resultCountry.set("avUsersPerChannel",Math.min(maxMembers,nbrUsers+1));
								} else {
									resultCountry.set("avUsersPerChannel",Math.min(maxMembers,Math.round((nbrUsers+1)/nbrChannels)));
								}
							}
						} else {
							resultCountry.increment("nbrUsers");
							if (nbrChannels == 0) {
							resultCountry.set("avUsersPerChannel",nbrUsers+1);
							} else {
							resultCountry.set("avUsersPerChannel",Math.min(maxMembers,Math.round((nbrUsers+1)/nbrChannels)));
							}		
						}
					request.object.set("customChannel",channelName);
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
					resultCountry.increment("nbrUsers");
					resultCountry.set("avUsersPerChannel",Math.min(maxMembers,Math.round((nbrUsers+1)/nbrChannels))); // Updating country (no new channel because it has never
															  														  // been full yet
					request.object.set("customChannel",country + "_" + nbrChannels);
					request.object.set("changeOfChannel",false);
					return Parse.Object.saveAll([newChannel,resultCountry]); // Saving country and channel
					
					}
				}, function(error) {
					console.error("Error while setting or changing channel for user " + request.object.get("username") + ". Error : " + error.code + " " + error.message); // If there is an error, it is logged;
				});
					
				}
				
		}).then(function(){
		response.success(); // Everything was successful
		},
		function (error){
		console.error("Error querying the country while changing the channel for user " + request.object.get("username") + ". Error : " + error.code + " " + error.message); // If there is an error, it is logged;
			});
	
	} else { // There is no change of channel, just saving the User
	
	response.success();

	}
	}
	}
	
});

////

//// User afterSave

// Updating all installations linked to the user being saved
Parse.Cloud.afterSave(Parse.User, function(request) {
	Parse.Cloud.useMasterKey();
	var channel = request.object.get("customChannel");
	var subscribedChannels = request.object.get("subscribedChannels");
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
		installation.set("customChannel",channel); // Updating the channel
		installation.set("channels",subscribedChannels); // Updating the subscribedChannels
		installation.set("useLocation",useLocation); // Updating the location setting
		installation.set("locationKnown",locationKnown); // Updating the location knowledge status
		installation.set("international", international); // Updating the international setting
		installation.set("internationalChannel", internationalChannel); //Updating the international channel
		installation.save();
	});
	}
});

////

//// usersAround

// Calculating the number of users withing a certain radius of the user
Parse.Cloud.define("usersAround", function(request, response) {
  Parse.Cloud.useMasterKey();
  var userGeoPoint = request.user.get("location"); // Getting the location of the user
  var radius = request.params.radius; // Getting the radius set by the user in the progressBar in the Submit class of the app
  var userQuery = new Parse.Query(Parse.User); // Querying the users
  userQuery.equalTo("useLocation", true); // Keeping only the users who have allowed the use of their location
  userQuery.equalTo("locationKnown",true); // Keeping only those whose location is not obsolete
  userQuery.withinKilometers("location", userGeoPoint, radius); // Keeping only those withing "radius" km of the user making the request
  userQuery.count().then(function(number){ // Counting those who are left
	response.success(number); // Sending the number back to the app
	}, function(error) {
	console.error("Error while calculating the number of users around the asker " + request.user.get("username") + ". Error : " + error.code + " " + error.message); // If there is an error, it is logged;
	});
});

////

//// addSubscriber

// Incrementing the number of subscribers of a particular user
Parse.Cloud.define("addSubscriber", function(request, response) {
  Parse.Cloud.useMasterKey();
  var query = new Parse.Query(Parse.User);
  query.equalTo("username",request.params.username); // Querying the user whose username is in the parameters given by the app
  query.first().then(function(asker) {
  	asker.increment("nbrSubscribers"); // Updating the number of subscribers
  	asker.save().then(function(){ // Saving the user
  		response.success();
  	});
  },
	function(error) {
      response.error("Unable to subscribe");
      console.error("Error subscribing new user to " + request.params.username + " Error : " + error); // If there is an error, it is logged
    });
});

////

//// subtractSubscriber

// Subtracting 1 to the number of subscribers of a particular user
Parse.Cloud.define("subtractSubscriber", function(request, response) {
  Parse.Cloud.useMasterKey();
  var query = new Parse.Query(Parse.User);
  query.containedIn("username",request.params.usernames); // Querying the user whose username is in the parameters given by the app
  query.each(function(asker) {
  	asker.increment("nbrSubscribers",-1); // Updating the number of subscribers
  	return asker.save(); // Saving the user
  }).then(function() {
  	response.success();
  	},
	function(error) {
      response.error("Unable to unsubscribe");
      console.error("Error unsubscribing new user from " + request.params.username + " Error : " + error); // If there is an error, it is logged
    });
});

////

//// usersLocation

// The job usersLocation sets locationKnown to false for all users who haven't been updated in more than a certain time.
// The related installations are automatically updated by the afterSave function.
Parse.Cloud.job("usersLocation", function(request, status) {
	Parse.Cloud.useMasterKey();
    
    var now = new Date(); // Current date
    var maxTimeSinceLastUpdate = 86400000 // Maximum time (ms) elapsed since last update before the location is considered obsolete
    var minLastUpdate = now.getTime() - maxTimeSinceLastUpdate; // The corresponding minimum time of last update

    var userQuery = new Parse.Query(Parse.User); 
	userQuery.equalTo("locationKnown",true); // Querying the users whose location is supposedly known
	userQuery.lessThan("lastUpdate",minLastUpdate); // Keeping only those that haven't been updated in more than maxTimeSinceLastUpdate ms
	userQuery.each(function(user) {

		user.set("locationKnown",false); // For those users, the location is now unknown
		return user.save();
	
		}).then(function() {
		status.success("Users known location successfully updated.");
		}, function(error) {
		status.error("Oops, something went wrong!");
		});
});

////

//// savedQuestions

// Sending the saved questions of the current user to his terminal so that he can view them
Parse.Cloud.define("savedQuestions", function(request, response) {
  Parse.Cloud.useMasterKey();
  var relation = request.user.relation("savedQuestions"); // Getting the location of the user
  var questionsPerPage = 10;
  var questionsJSONArray = [];  
  var firstPage = request.params.firstPage;
  var query = relation.query();

  query.skip(questionsPerPage*(firstPage-1));
  query.limit(questionsPerPage);
  query.descending("lastUpdate");

  query.find().then(function(results){
  	
  	for (i=0;i<results.length;i++) {
  		questionsJSONArray.push({
  			"questionID" : results[i].id,
  			"questionText" : results[i].get("text"),
  			"country" : results[i].get("country"),
  			"createdAt": results[i].createdAt,
			"nbrAnswers" : results[i].get("nbrAnswers"),
			"nA": results[i].get("nA"),
			"nA1": results[i].get("nA1"),
			"nA2": results[i].get("nA2"),
			"nA3": results[i].get("nA3"),
			"nA4": results[i].get("nA4"),
			"nA5": results[i].get("nA5"),
			"pcA1": results[i].get("pcA1"),
			"pcA2": results[i].get("pcA2"),
			"pcA3": results[i].get("pcA3"),
			"pcA4": results[i].get("pcA4"),
			"pcA5": results[i].get("pcA5"),
			"answer1": results[i].get("answer1"),
			"answer2": results[i].get("answer2"),
			"answer3": results[i].get("answer3"),
			"answer4": results[i].get("answer4"),
			"answer5": results[i].get("answer5"),
			"international": results[i].get("international"),
			"around": results[i].get("around"),
			"radius": results[i].get("radius"),
			"subscribersOnly": results[i].get("subscribersOnly"),
			"askerUsername": results[i].get("askerUsername")
  		})
  	}

  	response.success(questionsJSONArray);

  	},function(error){
  	response.error("No more questions to load!");
  	console.error("Error while sending savedQuestions to user " + request.user.get("username") + ". Error : " + error.code + " " + error.message); // If there is an error, it is logged;
  	});

});

////

//// deleteSavedQuestions

// Deleting the saved questions the user decided to delete
Parse.Cloud.define("deleteSavedQuestions", function(request, response) {
  Parse.Cloud.useMasterKey();
  var relation = request.user.relation("savedQuestions"); // Getting the location of the user
  var questions = request.params.questions;

  	var query = new Parse.Query("Question");
  	query.containedIn("objectId",questions);
	query.find().then(function(results){

	relation.remove(results);

  	return request.user.save();

  	}).then(function(){
  		response.success();
  	},function(error){
  	console.error("Error while deleting savedQuestions to user " + request.user.get("username") + ". Error : " + error.code + " " + error.message); // If there is an error, it is logged;
  	});

});

////

//// userExists

// Testing if a particular user exists (for subscription purposes)

Parse.Cloud.define("userExists", function(request, response) {
  Parse.Cloud.useMasterKey();
  var query = new Parse.Query(Parse.User);
  query.equalTo("username",request.params.username); // Querying the user whose username is in the parameters given by the app
  query.first().then(function(user) {
  	response.success((user!=null));
  },
	function(error) {
      response.error("Unable to look for user");
      console.error("Error looking for user " + request.params.username + " Error : " + error); // If there is an error, it is logged
    });
});

////

//////



////// II - Questions: newQuestion, beforeSave, afterSave, addapproval, addAnswer, questionsDeleteForLackOfApproval and questionsDeleteForLackOfAnswers


//// newQuestion

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
  var subscribersOnly = request.params.subscribersOnly;
  var nbrUsersTargeted = request.params.nbrUsersTargeted;
  var around = request.params.around;
  var radius = request.params.radius; // Getting the parameters set by the User in the app

  switch (nbrAnswers) { // 4 cases depending on the number of possible answers to the question
	case 2:
		var newQuestion = new Parse.Object("Question");
		newQuestion.set("asker",asker); // The user who asked the question
		newQuestion.set("askerUsername",asker_username); // His/her username (so that the user getting the question doesn't have access to the asker's Id and other fields)
		newQuestion.set("country",country); // The country for the question ("International" is also a country)
		newQuestion.set("text",text); // The question per say
		newQuestion.set("approved",0); // Number of users who approved the question being broadcast
		newQuestion.set("rejected",0); // Number of users who rejected the question being broadcast
		newQuestion.set("nbrAnswers",nbrAnswers); // Number of possible answers to the question
		newQuestion.set("answer1",request.params.answer1); // Text for the first possible answer
		newQuestion.set("answer2",request.params.answer2); // Text for the second possible answer
		newQuestion.set("answer3",""); 
		newQuestion.set("answer4","");
		newQuestion.set("answer5",""); // No third, fourth or fifth answers in this case
		newQuestion.set("nA",0); // Number of users who answered
		newQuestion.set("nA1",0); // Number of users who answered answer 1
		newQuestion.set("nA2",0); // Number of users who answered answer 2
		newQuestion.set("nA3",0); // Number of users who answered answer 3
		newQuestion.set("nA4",0); // Number of users who answered answer 4
		newQuestion.set("nA5",0); // Number of users who answered answer 5
		newQuestion.set("numberOfChannels",nbrChannels); // Number of channels to which the question will be sent (as chosen by the asker using the progressBar in the Submit class of the app)
		newQuestion.set("channels", []); // The array that will save to which channels the question has been sent, so that the same people who received the question will receive the results
		newQuestion.set("toSendForApproval",true); // If true, then the question hasn't been approved yet and needs to be sent for approval to a portion of the targeted users
		newQuestion.set("approvedAndSent",false); // If true, then the question has been approved and now needs to be sent to all users of the channels in the array "channels" so that they can answer
		newQuestion.set("published",false); // If true, then the results have been published, the question has finished its life cycle 
		newQuestion.set("international",international); // If true, then the question is an international one, meaning it is in english and targets internationalChannels only
		newQuestion.set("subscribersOnly",subscribersOnly); // If true, then the question targets only the asker's subscribers
		newQuestion.set("nbrUsersTargeted",nbrUsersTargeted); // The total number of users targeted by the question
		newQuestion.set("around",around); // If true, then the question is asked around the asker using localization to determine the number of users in a certain radius of the asker
		newQuestion.set("radius",radius); // The radius around the asker used to find the targeted users
		newQuestion.set("approvedOrRejectedBy",[]); // Initializing the approvedOrRejectedBy and answeredBy arrays to the empty array so that it is not null for the addAnswer and addApproval functions
		newQuestion.set("answeredBy",[]);
		newQuestion.save().then(function(){ // Saving the question
		response.success();
		}, function(error){
		console.error("Error while creating a quesion with 2 answers for asker " + asker_username+ ". Error : " + error.code + " " + error.message); // If there is an error, it is logged;
		});
	break;
	case 3:
		var newQuestion = new Parse.Object("Question");
		newQuestion.set("asker",asker);
		newQuestion.set("askerUsername",asker_username);
		newQuestion.set("country",country);
		newQuestion.set("text",text);
		newQuestion.set("approved",0);
		newQuestion.set("rejected",0);
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
		newQuestion.set("toSendForApproval",true);
		newQuestion.set("published",false);
		newQuestion.set("approvedAndSent",false);
		newQuestion.set("international",international);
		newQuestion.set("subscribersOnly",subscribersOnly);
		newQuestion.set("nbrUsersTargeted",nbrUsersTargeted);
		newQuestion.set("around",around);
		newQuestion.set("radius",radius);
		newQuestion.set("approvedOrRejectedBy",[]); // Initializing the approvedOrRejectedBy and answeredBy arrays to the empty array so that it is not null for the addAnswer and addApproval functions
		newQuestion.set("answeredBy",[]); // Setting the question's fields and saving
		newQuestion.save().then(function(){
		response.success();
		}, function(error){
		console.error("Error while creating a quesion with 3 answers for asker " + asker_username+ ". Error : " + error.code + " " + error.message); // If there is an error, it is logged;
		});
	break;
	case 4:
		var newQuestion = new Parse.Object("Question");
		newQuestion.set("asker",asker);
		newQuestion.set("askerUsername",asker_username);
		newQuestion.set("country",country);
		newQuestion.set("text",text);
		newQuestion.set("approved",0);
		newQuestion.set("rejected",0);
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
		newQuestion.set("toSendForApproval",true);
		newQuestion.set("published",false);
		newQuestion.set("approvedAndSent",false);
		newQuestion.set("international",international);
		newQuestion.set("subscribersOnly",subscribersOnly);
		newQuestion.set("nbrUsersTargeted",nbrUsersTargeted);
		newQuestion.set("around",around);
		newQuestion.set("radius",radius);
		newQuestion.set("approvedOrRejectedBy",[]); // Initializing the approvedOrRejectedBy and answeredBy arrays to the empty array so that it is not null for the addAnswer and addApproval functions
		newQuestion.set("answeredBy",[]); // Setting the question's fields and saving
		newQuestion.save().then(function(){
		response.success();
		}, function(error){
		console.error("Error while creating a quesion with 4 answers for asker " + asker_username+ ". Error : " + error.code + " " + error.message); // If there is an error, it is logged;
		});
	break;
	case 5:
		var newQuestion = new Parse.Object("Question");
		newQuestion.set("asker",asker);
		newQuestion.set("askerUsername",asker_username);
		newQuestion.set("country",country);
		newQuestion.set("text",text);
		newQuestion.set("approved",0);
		newQuestion.set("rejected",0);
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
		newQuestion.set("toSendForApproval",true);
		newQuestion.set("published",false);
		newQuestion.set("approvedAndSent",false);
		newQuestion.set("international",international);
		newQuestion.set("subscribersOnly",subscribersOnly);
		newQuestion.set("nbrUsersTargeted",nbrUsersTargeted);
		newQuestion.set("around",around);
		newQuestion.set("radius",radius);
		newQuestion.set("approvedOrRejectedBy",[]); // Initializing the approvedOrRejectedBy and answeredBy arrays to the empty array so that it is not null for the addAnswer and addApproval functions
		newQuestion.set("answeredBy",[]); // Setting the question's fields and saving
		newQuestion.save().then(function(){
		response.success();
		}, function(error){
		console.error("Error while creating a quesion with 5 answers for asker " + asker_username+ ". Error : " + error.code + " " + error.message); // If there is an error, it is logged;
		});
	break;
		response.error("Unable to submit question");
		console.error("Error while creating a quesion for asker " + asker_username+ ". Error : " + error.code + " " + error.message); // If there is an error, it is logged;
  }
});

////

//// Question beforeSave

// Setting some of the question's fields depending on its life cycle's advancement
Parse.Cloud.beforeSave("Question", function(request,response){
	Parse.Cloud.useMasterKey();
	var n = request.object.get("nbrAnswers");
	var nI = request.object.get("approved");
	var nA = request.object.get("nA");
	var subscribersOnly = request.object.get("subscribersOnly");
	var approvedAndSent = request.object.get("approvedAndSent");
	var published = request.object.get("published");
	var nbrUsersTargeted = request.object.get("nbrUsersTargeted"); // Getting the required values from the question
	var minToA = Math.max(1,Math.floor((nbrUsersTargeted*3)/100)); // Calculating the minimum number of users who have to approve the question before it is broadcast: 50% of the targeted users
	var minForR = Math.max(1,Math.floor((nbrUsersTargeted*50)/100)); // Calculating the minimum number of users who have to answer the question before the results are published: 50% of the targeted users
	request.object.set("minToBeApproved",minToA); // Setting the two numbers
	request.object.set("minForResults",minForR);
	var now = new Date(); // Current date
	request.object.set("lastUpdate",now.getTime()); // Setting the date of the question's lastUpdate
	if (subscribersOnly) { // Bypassing the request for approval for questions to subscribers only
		nI = minToA;
		request.object.set("toSendForApproval",false);
	}
	if (nI == minToA && !approvedAndSent) { // If the number of users who approve the question is the required minToBeApproved and if the question hasn't been broadcast yet
		request.object.set("toSendForAnswer",true); // The question needs to be broadcast to the targeted users so that they can answer
		} else {
		request.object.set("toSendForAnswer",false); // The question mustn't be broadcast yet or has already been broadcast
		}
	if (nA == minForR && !published) { // If the number of users who answered the question is the required minForResults and if the question hasn't been published yet
		request.object.set("toSendForResults",true); // The question needs to be published
		} else {
		request.object.set("toSendForResults",false); // There aren't enough answers for publication, or it has been published already
		}
		
	if (nA == 0){ // Converting the number of answers for each choice to percentiles, if it is equal to 0 or not (the two are separate because if nA = 0, then the fiedls nA1 to nA5 are null)
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

////


//// Question afterSave

// The core of the app: depending on the variables set in beforeSave, the question is either sent to users for approval, answers or to give them the results, or nothing happens
Parse.Cloud.afterSave("Question", function(request){
	Parse.Cloud.useMasterKey();
	var time_for_approval = 1800000; // expiration time for notification requesting approval: necessary because of unwanted delays for push notifications depending on the target terminals
	var time_for_answer = 3600000; // expiration time for notification requesting answers
	var time_for_results = 86400000; // expiration time for notification giving results
	var percentTargetedForApproval = 8;
	
	var subscribersOnly = request.object.get("subscribersOnly"); // True if the question targets the asker's subscribers only
	var international = request.object.get("international"); // True if the question is International
	if (international) {
	var country = "International"; // If the question is International, then the "country" is set as "International"
	} else {
	var country = request.object.get("country"); // If not, then the country is the same as the question's "country" field
	}
	var around = request.object.get("around"); // True if the question is sent to the users in a certain radius around the asker
	var toSendForApproval = request.object.get("toSendForApproval"); 
	var toSendForAnswer = request.object.get("toSendForAnswer");
	var toSendForResults = request.object.get("toSendForResults"); // Values set in question beforeSave to determine where the question is in its life cycle

	if (toSendForApproval || toSendForAnswer || toSendForResults) { // If either of the three is true, then a notification needs to be sent; if not, then nothing needs to happen
		
	if (toSendForApproval) { // The question needs to be sent for approval (it has just been created)

	if (around) { // If the question is for users around the asker
	
	var asker = request.object.get("asker"); // Getting the asker
	asker.fetch({ // Fetching the asker's location
  	success: function(user) { // The fetch was succesful
    var userGeoPoint = user.get("location"); // Asker's location
	var radius = request.object.get("radius"); // Radius around the asker
	var pushQuery = new Parse.Query(Parse.Installation); // Querying the Installations
	pushQuery.equalTo("useLocation", true); // Only Installations on which the user agreed to use his/her location
	pushQuery.equalTo("locationKnown",true); // Only if the location of the Installations is recent enough
	pushQuery.withinKilometers("location", userGeoPoint, radius); // Finding the Installations inside the set radius around the asker
	pushQuery.find().then(function(results) { // If the Installations have been found succesfully
		var subscribers = new Array(results.length);
		for (i=0;i<results.length;i++) {
			subscribers[i]=results[i].id;
			//request.object.add("Subscribers",results[i].id); // The Installations found (their objectId's) are saved in the question object, so that they are the same which will receive the notification for answer and the results
		}
		pushQuery.limit(Math.max(1,Math.floor(results.length*percentTargetedForApproval/100))); // Limiting to a certain number of users since it is only a notification for approval
		return Parse.Push.send({
		where: pushQuery, // Sending to the resulting Installations
		data: {
			action: "com.spersio.opinion.APPROVAL", // Intent-filter for the CustomPushReceiver in the app
			// No alert or title needed because the notification is built by the CustomPushReceiver
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
			askerUsername: request.object.get("askerUsername") // All the data needed by the app when receiving the push
		},
		expiration_interval: time_for_approval // Expiration time for the notification
		}, {
		success: function(){
			request.object.set("Subscribers",subscribers);
			request.object.set("toSendForApproval",false); // If the push succeeded, toSendForApproval is set to false so that the notification isn't sent again: the life cycle of the question advances
			request.object.save(); // Saving the question
			},
		error: function(error) {
			console.error("Error sending for Approval to users around the asker: questionID = " + request.object.id + " Error : " + error.code + " " + error.message); // If there is an error, it is logged
		}	
		});
	}, function (error) {
		console.error("Error querying for the Installations around the asker: questionID = " + request.object.id + " Error : " + error.code + " " + error.message); // If there is an error, it is logged;
		}
	);
  	}
	});
	
	} else { // If the question is for users in the country of the asker

	var numberOfChannels = request.object.get("numberOfChannels"); // Number of channels to which the question will be sent (as chosen by the asker using the progressBar in the Submit class of the app)
	var nbrUsersTargeted = request.object.get("nbrUsersTargeted"); // Number of users targeted by the questions
	var range = Math.floor(Math.max(1,nbrUsersTargeted*percentTargetedForApproval/100));
	var skip = Math.floor(Math.random()*Math.floor(nbrUsersTargeted/range))*range;
	console.log(range);
	console.log(skip);
	var nbrChannel = 0; // This variable will be used to store the number of the current channel to be added to the "channels" array of the question
	var channels = new Array(numberOfChannels); // This array will be put in the "channels" field of the question, once it is filled
	var countryQuery = new Parse.Query("Country"); // Querying the countries
	countryQuery.equalTo("name",country); // Only the required country
	countryQuery.first({ // Only one country should match, so it is always the first
	success: function(result) { // Result countains the country
	var nbrChannels = result.get("nbrChannels"); // The number of existing channels (which have been full at least once) for the country
	var alreadyUsedChannels = new Array(nbrChannels+1); // An array used to know if a certain channel has already been added to the "channels" for this question
		for (i=0;i<nbrChannels+1;i++){
		alreadyUsedChannels[i]=0; // All zeroes for now: no channel has been added to "channels" yet
		}
	
	for (j=0;j<numberOfChannels;j++){ // Code to run as many times as there are targeted channels
	
	do {
	nbrChannel = Math.floor(Math.random() * nbrChannels); // nbrChannel is set to a random number between 0 and nbrChannels, effectively choosing a random channel (that has already been full at least once) for the country
	}
	while (alreadyUsedChannels[nbrChannel]==1); // The random draw is repeated as long as it hits a channel that has already been added to "channels"
	channels[nbrChannel] = country + '_' + nbrChannel; // Once it has drawn a new channel, this channel is added to "channels"
	alreadyUsedChannels[nbrChannel]=1; // And the alreadyUsedChannels array is updated to take it into account
	// This whole process is repeated numberOfChannels times, so that at the end "channels" countains exactly numberOfChannels different channels
	}

	var pushQuery = new Parse.Query(Parse.Installation); // Querying the Installations
	if (international) { // If the question is international
	pushQuery.containedIn("internationalChannel", channels); // Keeping only the installations whose internationalChannel matches one of the channels in the array "channels"
	} else {
	pushQuery.containedIn("customChannel",channels); // Keeping only the installations whose channel matches one of the channels in the array "channels"
	}
	pushQuery.skip(skip); // Keeping a random sample of these installations to which the request for approval is to be sent: skipping the first "skip" installations
	pushQuery.limit(Math.max(1,range)); // And taking only the "range" next ones
	Parse.Push.send({
		where: pushQuery, // Sending the notification the corresponding installations
		data: {
			action: "com.spersio.opinion.APPROVAL", // Intent-filter for the CustomPushReceiver in the app
			// No alert or title needed because the notification is built by the CustomPushReceiver
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
		expiration_interval: time_for_approval // Expiration time for the notification
		}, {
	success: function(){
	request.object.set("toSendForApproval",false); // If the push succeeded, toSendForApproval is set to false so that the notification isn't sent again: the life cycle of the question advances
	request.object.set("channels",channels); // The "channels" array is saved into the "channels" field of the question: this way in the next step of the life cycle, the targeted installations will be the same
	request.object.save(); // Saving the question
		},
	error: function(error) {
		console.error("Error sending for Approval to users in the asker's country: questionID = " + request.object.id + " Error : " + error.code + " " + error.message); // If there is an error, it is logged
	}	
	});

	},
	error: function(error) {
    	console.error("Error querying for the installations in the asker's country: questionID = " + request.object.id + " Error : " + error.code + " " + error.message); // If there is an error, it is logged;
	}
	});
	}
	}
	//}
	
	if (toSendForAnswer) { // The question needs to be broadcast for answers
	
	
	if (subscribersOnly) { // If it is for subscribers only (easiest case)

	var asker = request.object.get("asker");

	Parse.Push.send({
		channels: [request.object.get("askerUsername")], // All subscribers of the asker belong to the same channel and they are all targeted at once
		data: {
			action: "com.spersio.opinion.ANSWER",
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
			request.object.set("toSendForAnswer",false); // The life cycle advances: the question has been approved and sent
			request.object.set("approvedAndSent",true);
			request.object.save();
			},
		error: function(error) {
			console.error("Error sending for Answers to subscribers only: questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
		}	
	});
	var pushAskerQuery = new Parse.Query(Parse.Installation); // Querying Installations
	pushAskerQuery.equalTo("user",asker); // Finding the installations of the asker
	Parse.Push.send({
		where: pushAskerQuery, // Sending a notification to the asker
		data: {
			alert: request.object.get("text"),
			title: "Question sent"
		},
		expiration_interval: time_for_answer 
		}, {
		success: function(){
			},
		error: function(error) {
			console.error("Error telling asker the question has been sent (subscribers only): questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
		}	
	});

	} else {

	if (around) {
	

	var asker = request.object.get("asker");

	var pushQuery = new Parse.Query(Parse.Installation);
	pushQuery.containedIn("objectId",request.object.get("Subscribers")); // Getting the installations to which the approval request has been sent (before limiting to a certain number of users)
	Parse.Push.send({
		where: pushQuery,
		data: {
			action: "com.spersio.opinion.ANSWER",
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
			request.object.set("toSendForAnswer",false); // The life cycle advances: the question has been approved and sent
			request.object.set("approvedAndSent",true);
			request.object.save();
			},
		error: function(error) {
			console.error("Error sending for Answers to users around the asker: questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
		}	
	});
	var pushAskerQuery = new Parse.Query(Parse.Installation);
	pushAskerQuery.equalTo("user",asker);
	Parse.Push.send({
		where: pushAskerQuery,
		data: {
			alert: request.object.get("text"),
			title: "Question approved and sent"
		},
		expiration_interval: time_for_answer
		}, {
		success: function(){
			},
		error: function(error) {
			console.error("Error telling asker the question is approved and sent (around): questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
		}	
	});
	
	} else { 
	
	var channels = request.object.get("channels"); // Getting the targeted channels from the question
	var asker = request.object.get("asker");
	var pushQuery = new Parse.Query(Parse.Installation);
	if (international) {
	pushQuery.containedIn("internationalChannel",channels);
	} else {
	pushQuery.containedIn("customChannel",channels);
	}
	Parse.Push.send({
		where: pushQuery,
		data: {
			action: "com.spersio.opinion.ANSWER",
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
		request.object.set("toSendForAnswer",false); // The life cycle advances: the question has been approved and sent
		request.object.set("approvedAndSent",true);
		request.object.save();
		},
	error: function(error) {
		console.error("Error sending for Answers to users in the asker's country: questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
	}	
		});
	var pushAskerQuery = new Parse.Query(Parse.Installation);
		pushAskerQuery.equalTo("user",asker);
		Parse.Push.send({
		where: pushAskerQuery,
		data: {
			alert: request.object.get("text"),
			title: "Question approved and sent"
			},
		expiration_interval: time_for_answer
		}, {
		success: function(){
		},
		error: function(error) {
			console.error("Error telling asker the question is approved and sent (country): questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
		}	
		});
	}
	}
	}	
		
	if (toSendForResults) { // Enough users have answered, the results can be published to all the targeted users + the asker
	
	if (subscribersOnly) {

	var asker = request.object.get("asker");

	Parse.Push.send({
		channels: [request.object.get("askerUsername")],
		data: {
			action: "com.spersio.opinion.RESULTS",
			questionID: request.object.id,
			createdAt: request.object.createdAt,
			country: request.object.get("country"),
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
			subscribersOnly: subscribersOnly,
			askerUsername: request.object.get("askerUsername")
		},
		expiration_interval: time_for_results		
		}, {
		success: function(){
			request.object.set("toSendForResults",false); // The life cycle reaches its end: the results of the question have been published
			request.object.set("published",true);
			request.object.save();
			},
		error: function(error) {
			console.error("Error sending the results to the asker's subscribers: questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
		}	
			});
	var pushAskerQuery = new Parse.Query(Parse.Installation);
	pushAskerQuery.equalTo("user",asker);
	Parse.Push.send({
		where: pushAskerQuery,
		data: {
			action: "com.spersio.opinion.RESULTS",
			questionID: request.object.id,
			createdAt: request.object.createdAt,
			country: request.object.get("country"),
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
			subscribersOnly: subscribersOnly,
			askerUsername: request.object.get("askerUsername")
		},
		expiration_interval: time_for_results
		}, {
		success: function(){
			},
		error: function(error) {
			console.error("Error sending the results to the asker (subscribersOnly): questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
		}	
			});

	} else {

	if (around) {
	
	var asker = request.object.get("asker");

	var pushQuery = new Parse.Query(Parse.Installation);
	pushQuery.containedIn("objectId",request.object.get("Subscribers"));
	Parse.Push.send({
		where: pushQuery,
		data: {
			action: "com.spersio.opinion.RESULTS",
			questionID: request.object.id,
			createdAt: request.object.createdAt,
			country: request.object.get("country"),
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
			subscribersOnly: subscribersOnly,
			askerUsername: request.object.get("askerUsername")
		},
		expiration_interval: time_for_results		
		}, {
		success: function(){
			request.object.set("toSendForResults",false);
			request.object.set("published",true);
			request.object.save();
			},
		error: function(error) {
			console.error("Error sending the results to the users around the asker: questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
		}	
			});
	var pushAskerQuery = new Parse.Query(Parse.Installation);
	pushAskerQuery.equalTo("user",asker);
	Parse.Push.send({
		where: pushAskerQuery,
		data: {
			action: "com.spersio.opinion.RESULTS",
			questionID: request.object.id,
			createdAt: request.object.createdAt,
			country: request.object.get("country"),
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
			subscribersOnly: subscribersOnly,
			askerUsername: request.object.get("askerUsername")
		},
		expiration_interval: time_for_results
		}, {
		success: function(){
			},
		error: function(error) {
			console.error("Error sending the results to the asker (around): questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
		}	
			});
	
	} else {
	
	var channels = request.object.get("channels");
	var asker = request.object.get("asker");	
	var pushQuery = new Parse.Query(Parse.Installation);
	if (international) {
	pushQuery.containedIn("internationalChannel",channels);
	} else {
	pushQuery.containedIn("customChannel",channels);
	}
	Parse.Push.send({
		where: pushQuery,
		data: {
			action: "com.spersio.opinion.RESULTS",
			questionID: request.object.id,
			createdAt: request.object.createdAt,
			country: request.object.get("country"),
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
			subscribersOnly: subscribersOnly,
			askerUsername: request.object.get("askerUsername")
		},
		expiration_interval: time_for_results
		}, {
	success: function(){
		request.object.set("toSendForResults",false);
		request.object.set("published",true);
		request.object.save();
		},
	error: function(error) {
		console.error("Error sending the results to the users in the asker's country: questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
	}	
		});
	var pushAskerQuery = new Parse.Query(Parse.Installation);
	pushAskerQuery.equalTo("user",asker);
	Parse.Push.send({
		where: pushAskerQuery,
		data: {
			action: "com.spersio.opinion.RESULTS",
			questionID: request.object.id,
			createdAt: request.object.createdAt,
			country: request.object.get("country"),
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
			subscribersOnly: subscribersOnly,
			askerUsername: request.object.get("askerUsername")
		},
		expiration_interval: time_for_results
		}, {
	success: function(){
		},
	error: function(error) {
		console.error("Error sending the results to the asker (country): questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
	}	
		});
	}
	}
	}
	}
});

////


//// addapproval

// Registering the approval or disapproval of the user for a question
Parse.Cloud.define("addApproval", function(request, response) {
  Parse.Cloud.useMasterKey();
  var query = new Parse.Query("Question");
  query.get(request.params.id, { // Getting the question whose ID has been given by the user's app
	success: function(question) {

  		var users = question.get("approvedOrRejectedBy");

  		if (users.indexOf(request.user.id)!=-1) {
  			response.error("Already approved or rejected question!"); // Testing to see if the user hasn't approved or rejected the question yet on another terminal
  		} else {

		if (request.params.approval == 1){ // If the user approves
		question.increment("approved"); // approved is incremented
		question.addUnique("approvedOrRejectedBy",request.user.id); // Updating the list of users who already approved or rejected the question
		question.save().then(function() { // Saving the question
		response.success();
		}, function(error) {
		response.error("Unable to register approval"); // If there is an error, it is sent back to the user
		console.error("Error registering approval for questionID = " + request.params.id + " Error : " + error); // If there is an error, it is logged
		});
		} else { // It he/she disapproves
		question.increment("rejected"); // rejected is incremented
		question.addUnique("approvedOrRejectedBy",request.user.id);
		question.save().then(function() { // Saving the question
		response.success();
		}, function(error) {
		response.error("Unable to register lack of approval"); // If there is an error, it is sent back to the user
		console.error("Error registering lack of approval for questionID = " + request.params.id + " Error : " + error); // If there is an error, it is logged
		});
		}
	}
	},
	error: function() {
      response.error("Unable to register approval or lack thereof"); // If there is an error, it is sent back to the user
      console.error("Error registering approval or lack thereof for questionID = " + request.params.id + " Error : " + error); // If there is an error, it is logged
    }
	});
});

////


//// addAnswer

// Registering the answer given by a user
Parse.Cloud.define("addAnswer", function(request, response) {
  Parse.Cloud.useMasterKey();
  var n = request.params.n; // n is the number of the answer given (1 to 5)
  var query = new Parse.Query("Question");
  query.get(request.params.id, { // Getting the question whose ID has been given by the user's app
	success: function(question) {
		
		var users = question.get("answeredBy");

  		if (users.indexOf(request.user.id)!=-1) {
  			response.error("Already answered the question!"); // Testing to see if the user hasn't answered yet on another terminal
  		} else {

		switch (n){ // Multiple cases depending on the value of n
			case 1: // Answer 1
				question.increment("nA"); // Incrementing the number of users who answered
				question.increment("nA1"); // Incrementing the number of users who answered Answer 1
				question.addUnique("answeredBy",request.user.id); // Updating the users who answered the question
				question.save().then(function(){ // Saving the question
				response.success();
				}, function(error){
				console.error("Error registering Answer 1: questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
				response.error("Unable to register answer");
				});
			break;
			case 2: // Answer 2
				question.increment("nA"); // Incrementing the number of users who answered
				question.increment("nA2"); // Incrementing the number of users who answered Answer 2
				question.addUnique("answeredBy",request.user.id);
				question.save().then(function(){
				response.success();
				}, function(error){
				console.error("Error registering Answer 2: questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
				response.error("Unable to register answer");
				});				
			break;
			case 3: // Answer 3
				question.increment("nA"); // Incrementing the number of users who answered
				question.increment("nA3"); // Incrementing the number of users who answered Answer 3
				question.addUnique("answeredBy",request.user.id);
				question.save().then(function(){
				response.success();
				}, function(error){
				console.error("Error registering Answer 3: questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
				response.error("Unable to register answer");
				});				
			break;
			case 4: // Answer 4
				question.increment("nA"); // Incrementing the number of users who answered
				question.increment("nA4"); // Incrementing the number of users who answered Answer 4
				question.addUnique("answeredBy",request.user.id);
				question.save().then(function(){
				response.success();
				}, function(error){
				console.error("Error registering Answer 4: questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
				response.error("Unable to register answer");
				});
			break;
			case 5: // Answer 5
				question.increment("nA"); // Incrementing the number of users who answered
				question.increment("nA5"); // Incrementing the number of users who answered Answer 5
				question.addUnique("answeredBy",request.user.id);
				question.save().then(function(){
				response.success();
				}, function(error){
				console.error("Error registering Answer 5: questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
				response.error("Unable to register answer");
				});
			break;
			default: 
				console.error("Error registering Answer (no value for the number of the answer): questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
				response.error("Unable to register answer");
		}
	}
	},
	error: function() {
      response.error("Unable to register answer");
    }
	});
});

////


//// questionsDeleteForLackOfApproval

// Deleting all obsolete questions for lack of approval after a certain amount of time
Parse.Cloud.job("questionsDeleteForLackOfApproval", function(request, status) {
	Parse.Cloud.useMasterKey();
    var now = new Date(); // Current date
    var maxTimeSinceLastUpdate = 1830000 // Maximum time (ms) elapsed since last update before the question is considered obsolete for lack of approval: 1 830 000 ms = 30 min 30 s
    var minLastUpdate = now.getTime() - maxTimeSinceLastUpdate; // The corresponding minimum time of last update
    var nbrQuestionsDeleted = 0;

    var questionQuery = new Parse.Query("Question"); // Querying the Questions
	questionQuery.lessThan("lastUpdate",minLastUpdate); // Keeping only those that haven't been updated in more than maxTimeSinceLastUpdate ms
	questionQuery.equalTo("approvedAndSent",false); // Keeping only those that haven't been approved and sent
	questionQuery.each(function(question) { // For each question
		
		var asker = question.get("asker"); // Getting the asker
		nbrQuestionsDeleted = nbrQuestionsDeleted + 1;		

		return question.destroy().then(function(myObject) { // Destroying the question
			var pushAskerQuery = new Parse.Query(Parse.Installation); // Querying the Installations
			pushAskerQuery.equalTo("user",asker); // Keeping only the asker's installations
			Parse.Push.send({ // Sending the asker a notification telling him that his question was deleted because it didn't get enough approval
			where: pushAskerQuery,
			data: {
				alert: "Question: " + question.get("text"),
				title: "Question rejected"
			},
			expiration_interval: 86400
			}, {
			success: function(){
			},
			error: function(error) {
				console.error("Error while sending deleted for lack of approval notification. Error: " + error); // If there is an error, it is logged
			}	
			});
		},function(object, error) {
			status.error("Couldn't destroy the question (lack of approval): " + object.id + " Error: " + error); // If there is an error, it is logged
		});
			
		}).then(function() {
		status.success("Questions deletion for lack of approval successful. " + nbrQuestionsDeleted + " questions deleted."); // Reporting success of the job
		}, function(error) {
		status.error("Oops, something went wrong!"); // If there is an error, it is logged
		});
});

////


//// questionsDeleteForLackOfAnswers

// Deleting all obsolete questions for lack of answers after a certain amount of time
Parse.Cloud.job("questionsDeleteForLackOfAnswers", function(request, status) {
	Parse.Cloud.useMasterKey();
    var now = new Date(); // Current date
    var maxTimeSinceLastUpdate = 3660000 // Maximum time (ms) elapsed since last update before the question is considered obsolete for lack of answers: 3 660 000 ms = 1 h 1 min
    var minLastUpdate = now.getTime() - maxTimeSinceLastUpdate; // The corresponding minimum time of last update
    var nbrQuestionsDeleted = 0;

    var questionQuery = new Parse.Query("Question"); // Querying the Questions
	questionQuery.lessThan("lastUpdate",minLastUpdate); // Keeping only those that haven't been updated in more than maxTimeSinceLastUpdate ms
	questionQuery.equalTo("published",false); // Keeping only those that haven't gotten enough answers yet
	questionQuery.each(function(question) { // For each question
		
		var asker = question.get("asker"); // Getting the asker
		nbrQuestionsDeleted = nbrQuestionsDeleted + 1;	

		return question.destroy().then(function(myObject) {	// Destroying the question
			var pushAskerQuery = new Parse.Query(Parse.Installation); // Querying the Installations
			pushAskerQuery.equalTo("user",asker); // Keeping only the asker's installations
			Parse.Push.send({ // Sending the asker a notification telling him that his question was deleted because it didn't get enough answers
			where: pushAskerQuery,
			data: {
				alert: "Question: " + question.get("text"),
				title: "Question not answered"
			},
			expiration_interval: 86400
			}, {
			success: function(){
			},
			error: function(error) {
				console.error("Error while sending deleted for lack of answers notification. Error: " + error); // If there is an error, it is logged
			}	
			});
		},function(object, error) {
			status.error("Couldn't destroy the question (lack of answers): " + object.id + " Error: " + error); // If there is an error, it is logged
		});
			
		}).then(function() {
		status.success("Questions deletion for lack of answers successful. " + nbrQuestionsDeleted + " questions deleted."); // Reporting success of the job
		}, function(error) {
		status.error("Oops, something went wrong!"); // If there is an error, it is logged
		});
});

////

//////

////// III - Channels: checkFullChannels

//// checkFullChannels

// Check if the "full" channels are really full, to run after maxMembers has been modified
Parse.Cloud.job("checkFullChannels", function(request, status) {
	Parse.Cloud.useMasterKey();

	var maxMembers = 3;

    var channelQuery = new Parse.Query("Channel"); // Querying the Channels
	channelQuery.equalTo("full",true); // Keeping only those that are considered full
	channelQuery.lessThan("members",maxMembers);
	channelQuery.each(function(channel) { // For each channel

		channel.set("full",false);
		return channel.save();
			
		}).then(function() {
		status.success("Successfully checked and updated full channels."); // Reporting success of the job
		}, function(error) {
		status.error("Oops, something went wrong!"); // If there is an error, it is logged
		});
});
//

////

//////

//////// End of CloudCode ////////