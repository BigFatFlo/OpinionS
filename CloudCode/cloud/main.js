require("cloud/app.js"); // calling app.js for website

// Cloud Code for Opinion //

//// User afterSave

// Updating all installations linked to the user being saved
Parse.Cloud.afterSave(Parse.User, function(request) {
	Parse.Cloud.useMasterKey();
	var channels = request.object.get("channels");
	if (typeof request.object.getACL() === "undefined") {
	var newACL = new Parse.ACL(); // No public read access for users => only the user can read (and write) his own data:
	newACL.setPublicReadAccess(false);
	request.object.setACL(newACL);
	request.object.save();
	} else {
	var query = new Parse.Query(Parse.Installation);
	query.equalTo("user",request.object);
	query.each(function(installation) {
		installation.set("channels",channels); // Updating the channels
		installation.save();
	});
	}
});

////


//// changeOfProfile

// Updating the counters after a user has changed his/her profile
Parse.Cloud.define("changeOfProfile", function(request, response) {
  Parse.Cloud.useMasterKey();
  var oldCharNumber = request.params.oldCharNumber.toString();
  var newCharNumber = request.params.newCharNumber.toString();
  var oldCountry = request.params.oldCountry;
  var newCountry = request.params.newCountry;
  var newProfile = request.params.newProfile;
  var changeOfCountry = request.params.changeOfCountry;
  var changeOfCharNumber = request.params.changeOfCharNumber;
  var changeOfInternational = request.params.changeOfInternational;
  var international = request.params.international;

  if (request.params.newCharNumber>1999) {
  	response.error("Invalid newCharNumber");
  }

  if (newProfile) {

  	var countryQuery = new Parse.Query("Country");
	  countryQuery.equalTo("name", newCountry);
	  countryQuery.first().then(function(country) {

	  		var nbrUsersForCountry = country.get("nbrUsers");
		  	if (nbrUsersForCountry.hasOwnProperty(newCharNumber)) {
	  			nbrUsersForCountry[newCharNumber] = nbrUsersForCountry[newCharNumber] + 1;
	  		} else {
	  			nbrUsersForCountry[newCharNumber] = 1;
	  		}
		  	country.set("nbrUsers",nbrUsersForCountry);
		  	country.increment("totalNbrUsers");

		  	return country.save();

	  	}).then(function() {

	  		if (international) {

	  		var internationalQuery = new Parse.Query("Country");
	  		internationalQuery.equalTo("name", "International");
	  		return internationalQuery.first().then(function(internationalCountry) {

	  			var nbrUsersForInternational = internationalCountry.get("nbrUsers");
	  			if (nbrUsersForInternational.hasOwnProperty(newCharNumber)) {
	  			nbrUsersForInternational[newCharNumber] = nbrUsersForInternational[newCharNumber] + 1;
		  		} else {
		  			nbrUsersForInternational[newCharNumber] = 1;
		  		}
			  	internationalCountry.set("nbrUsers",nbrUsersForInternational);
			  	internationalCountry.increment("totalNbrUsers");

		  		return internationalCountry.save();

	  		})
	  	}

	  	}).then(function() {
	  	response.success();
	  },
	  function(error) {
	  response.error("Unable to create profile");
	  console.error("Error creating profile, error : " + error); // If there is an error, it is logged
	});

  } else if (changeOfCountry) {

  	var countryQuery = new Parse.Query("Country");
	  countryQuery.containedIn("name", [oldCountry, newCountry]);
	  countryQuery.find().then(function(countries) {

	  	if (countries[0].get("name").valueOf() == oldCountry.valueOf()) {
	  		
	  		countries[0].increment("totalNbrUsers",-1);
	  		var nbrUsersForOldCountry = countries[0].get("nbrUsers");
		  	nbrUsersForOldCountry[oldCharNumber] = nbrUsersForOldCountry[oldCharNumber] - 1;
		  	countries[0].set("nbrUsers",nbrUsersForOldCountry);

	  		countries[1].increment("totalNbrUsers");
	  		var nbrUsersForNewCountry = countries[1].get("nbrUsers");
	  		if (nbrUsersForNewCountry.hasOwnProperty(newCharNumber)) {
	  			nbrUsersForNewCountry[newCharNumber] = nbrUsersForNewCountry[newCharNumber] + 1;
	  		} else {
	  			nbrUsersForNewCountry[newCharNumber] = 1;
	  		}
	  		countries[1].set("nbrUsers",nbrUsersForNewCountry);

	  	} else {

	  		countries[1].increment("totalNbrUsers",-1);
	  		var nbrUsersForOldCountry = countries[1].get("nbrUsers");
		  	nbrUsersForOldCountry[oldCharNumber] = nbrUsersForOldCountry[oldCharNumber] - 1;
		  	countries[1].set("nbrUsers",nbrUsersForOldCountry);

	  		countries[0].increment("totalNbrUsers");
	  		var nbrUsersForNewCountry = countries[0].get("nbrUsers");
	  		if (nbrUsersForNewCountry.hasOwnProperty(newCharNumber)) {
	  			nbrUsersForNewCountry[newCharNumber] = nbrUsersForNewCountry[newCharNumber] + 1;
	  		} else {
	  			nbrUsersForNewCountry[newCharNumber] = 1;
	  		}
	  		countries[0].set("nbrUsers",nbrUsersForNewCountry);

	  	}

	  	return Parse.Object.saveAll(countries);

	  }).then(function() {

	  	if (changeOfInternational) {

	  		var internationalQuery = new Parse.Query("Country");
	  		internationalQuery.equalTo("name", "International");
	  		
	  		return internationalQuery.first().then(function(internationalCountry) {

	  			var nbrUsersForInternational = internationalCountry.get("nbrUsers");
	  			
	  			if (international) {

	  				if (nbrUsersForInternational.hasOwnProperty(newCharNumber)) {
		  				nbrUsersForInternational[newCharNumber] = nbrUsersForInternational[newCharNumber] + 1;
			  		} else {
			  			nbrUsersForInternational[newCharNumber] = 1;
			  		}

			  		internationalCountry.increment("totalNbrUsers");

	  			} else {

		  			nbrUsersForInternational[oldCharNumber] = nbrUsersForInternational[oldCharNumber] - 1;
		  			internationalCountry.increment("totalNbrUsers",-1);

	  			}

			  	internationalCountry.set("nbrUsers",nbrUsersForInternational);

		  	return internationalCountry.save();

	  		});

	  	} else if (international && changeOfCharNumber) {

	  		var internationalQuery = new Parse.Query("Country");
	  		internationalQuery.equalTo("name", "International");
	  		return internationalQuery.first().then(function(internationalCountry) {

	  			var nbrUsersForInternational = internationalCountry.get("nbrUsers");

  				if (nbrUsersForInternational.hasOwnProperty(newCharNumber)) {
	  				nbrUsersForInternational[newCharNumber] = nbrUsersForInternational[newCharNumber] + 1;
		  		} else {
		  			nbrUsersForInternational[newCharNumber] = 1;
		  		}
		  		nbrUsersForInternational[oldCharNumber] = nbrUsersForInternational[oldCharNumber] - 1;

		  		internationalCountry.set("nbrUsers",nbrUsersForInternational);

			return internationalCountry.save();

			});

	  	}

	  	}).then(function() {
	  	response.success();
	  },
	  function(error) {
	  response.error("Unable to change profile");
	  console.error("Error changing profile, error : " + error); // If there is an error, it is logged
	})

  } else if (changeOfCharNumber) {

  	var countryQuery = new Parse.Query("Country");
	  countryQuery.equalTo("name", oldCountry);
	  countryQuery.first().then(function(country) {

	  		var nbrUsersForCountry = country.get("nbrUsers");
		  	nbrUsersForCountry[oldCharNumber] = nbrUsersForCountry[oldCharNumber] - 1;
		  	if (nbrUsersForCountry.hasOwnProperty(newCharNumber)) {
	  			nbrUsersForCountry[newCharNumber] = nbrUsersForCountry[newCharNumber] + 1;
	  		} else {
	  			nbrUsersForCountry[newCharNumber] = 1;
	  		}
		  	country.set("nbrUsers",nbrUsersForCountry);

	  return country.save();

	  }).then(function() {

	  	if (changeOfInternational) {

	  		var internationalQuery = new Parse.Query("Country");
	  		internationalQuery.equalTo("name", "International");
	  		return internationalQuery.first().then(function(internationalCountry) {

	  			var nbrUsersForInternational = internationalCountry.get("nbrUsers");
	  			
	  			if (international) {

	  				if (nbrUsersForInternational.hasOwnProperty(newCharNumber)) {
		  				nbrUsersForInternational[newCharNumber] = nbrUsersForInternational[newCharNumber] + 1;
			  		} else {
			  			nbrUsersForInternational[newCharNumber] = 1;
			  		}
			  		internationalCountry.increment("totalNbrUsers");

				} else {

					nbrUsersForInternational[oldCharNumber] = nbrUsersForInternational[oldCharNumber] - 1;
					internationalCountry.increment("totalNbrUsers",-1);

				}

				internationalCountry.set("nbrUsers",nbrUsersForInternational);

			return internationalCountry.save();

			});

		} else if (international) {

			var internationalQuery = new Parse.Query("Country");
			internationalQuery.equalTo("name", "International");
			return internationalQuery.first().then(function(internationalCountry) {

				var nbrUsersForInternational = internationalCountry.get("nbrUsers");

					if (nbrUsersForInternational.hasOwnProperty(newCharNumber)) {
						nbrUsersForInternational[newCharNumber] = nbrUsersForInternational[newCharNumber] + 1;
					} else {
						nbrUsersForInternational[newCharNumber] = 1;
					}
					nbrUsersForInternational[oldCharNumber] = nbrUsersForInternational[oldCharNumber] - 1;

					internationalCountry.set("nbrUsers",nbrUsersForInternational);

			return internationalCountry.save();

			});

	  	}

	  	}).then(function() {
	  	response.success();
	  },
	  function(error) {
	  response.error("Unable to change profile");
	  console.error("Error changing profile, error : " + error); // If there is an error, it is logged
	});

  } else if (changeOfInternational) {

  	var internationalQuery = new Parse.Query("Country");
  		internationalQuery.equalTo("name", "International");
  		internationalQuery.first().then(function(internationalCountry) {

  			var nbrUsersForInternational = internationalCountry.get("nbrUsers");
  			
  			if (international) {

  				if (nbrUsersForInternational.hasOwnProperty(newCharNumber)) {
	  				nbrUsersForInternational[newCharNumber] = nbrUsersForInternational[newCharNumber] + 1;
		  		} else {
		  			nbrUsersForInternational[newCharNumber] = 1;
		  		}
		  		internationalCountry.increment("totalNbrUsers");

			} else {

				nbrUsersForInternational[oldCharNumber] = nbrUsersForInternational[oldCharNumber] - 1;
				internationalCountry.increment("totalNbrUsers",-1);

			}

			internationalCountry.set("nbrUsers",nbrUsersForInternational);

		return internationalCountry.save();

		}).then(function() {
	  	response.success();
	  },
	  function(error) {
	  response.error("Unable to update international");
	  console.error("Error updating international, error : " + error); // If there is an error, it is logged
	});

  }

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
      console.error("Error unsubscribing user from " + request.params.username + " Error : " + error); // If there is an error, it is logged
    });
});

////


//// addMember

// Incrementing the number of members of a particular group
Parse.Cloud.define("addMember", function(request, response) {
  Parse.Cloud.useMasterKey();
  var query = new Parse.Query("Group");
  query.equalTo("name",request.params.groupname); // Querying the group whose name is in the parameters given by the app
  query.first().then(function(group) {
  	group.increment("nbrMembers"); // Updating the number of members
  	group.addUnique("members",request.params.username); // Updating the list of members
  	group.save().then(function(){ // Saving the group
  		response.success();
  	});
  },
	function(error) {
      response.error("Unable to add member");
      console.error("Error adding member to group " + request.params.groupname + " Error : " + error); // If there is an error, it is logged
    });
});

////


//// subtractMember

// Subtracting one member from a group
Parse.Cloud.define("subtractMember", function(request, response) {
  Parse.Cloud.useMasterKey();
  var query = new Parse.Query("Group");
  query.equalTo("name",request.params.groupname); // Querying the group whose name is in the parameters given by the app
  query.first().then(function(group) {
  	group.increment("nbrMembers",-1); // Updating the number of members
  	group.remove("members",Parse.User.current().get("username"));
  	return group.save(); // Saving the group
  }).then(function() {
  	response.success();
  	},
	function(error) {
      response.error("Unable to leave group");
      console.error("Error leaving group " + request.params.groupname + " Error : " + error); // If there is an error, it is logged
    });
});

////


//// createGroup

// Creating a new group
Parse.Cloud.define("createGroup", function(request, response) {
  Parse.Cloud.useMasterKey();
  var username = request.params.username;
  var query = new Parse.Query("Group");
  query.equalTo("name",request.params.groupname); // Querying the group whose name is in the parameters given by the app
  query.first().then(function(group) {
  	if (group!=null) {
  		response.success(true);
  	} else {
  		var newGroup = new Parse.Object("Group");
  		newGroup.set("name",request.params.groupname);
  		newGroup.set("owners",[username]);
  		newGroup.set("nbrMembers",0);
  		newGroup.set("members",[]);
  		newGroup.save().then(function(){ // Saving the group
  		response.success(false);
  		});
  	}
	},
	function(error) {
      response.error("Unable to create group " + request.params.groupname);
      console.error("Error creating group " + request.params.group + " Error : " + error); // If there is an error, it is logged
    });
});

////


//// nbrMembersInGroup

// Getting the number of members in a group, and checking if the user who makes the request is an owner of the group
Parse.Cloud.define("nbrMembersInGroup", function(request, response) {
  Parse.Cloud.useMasterKey();
  var query = new Parse.Query("Group");
  query.equalTo("name",request.params.groupname); // Querying the group whose name is in the parameters given by the app
  query.first().then(function(group) {
  	if (group==null) {
  		response.success(-1);
  	} else {
  		var owners = group.get("owners");
  		if (owners.indexOf(request.params.username)!=-1) {
  			response.success(group.get("nbrMembers"));
  		} else {
  			response.success(-2);
  		}
  	}
	},
	function(error) {
      response.error("Unable to get nbrMembers for group " + request.params.groupname);
      console.error("Error getting nbrMembers for group " + request.params.groupname + " Error : " + error); // If there is an error, it is logged
    });
});

////


//// addOwner

// Adding an owner to the group's owners list, on the request of one of the owners
Parse.Cloud.define("addOwner", function(request, response) {
  Parse.Cloud.useMasterKey();

  var groupname = request.params.groupname;
  var username = request.params.username;
  var newOwnerUsername = request.params.newOwnerUsername;

  if (Parse.User.current().get("ownedGroups").indexOf(groupname)==-1) {
  	
  	response.success(-4); // The current user isn't an owner of the group!

  } else {

  var userQuery = new Parse.Query(Parse.User);
  userQuery.equalTo("username",newOwnerUsername);
  userQuery.first().then(function(user) {

  	if (user==null) {

  		response.success(-3); // No user found for the username given by the current user!

  	} else {

  		if (user.get("ownedGroups") != null) {

  		if (user.get("ownedGroups").indexOf(groupname)!=-1) {

  			response.success(-2); // Requested user is already an owner of the group!

  		}

  		} else {

  			var query = new Parse.Query("Group");
			query.equalTo("name",groupname); // Querying the group whose name is in the parameters given by the app
			return query.first().then(function(group) {
				if (group==null) {

					response.success(-1); // The group doesn't exist!

				} else {

					var owners = group.get("owners");
					if (owners.indexOf(username)!=-1) {
						
						if (owners.indexOf(newOwnerUsername)==-1) {

							group.addUnique("owners",newOwnerUsername); // Everything is good, adding the new owner to the group
							user.addUnique("ownedGroups",groupname); // and the new group to the new owner
							return Parse.Object.saveAll([group,user]).then(function(){
								
								var pushQuery = new Parse.Query(Parse.Installation); // Querying Installations
								pushQuery.equalTo("user",user); // Finding the installations of the new owner
								return Parse.Push.send({
									where: pushQuery, // Sending a notification to the asker
									data: {
										alert: username + " made you an owner!",
										title: "Group " + groupname
									}
									}, {
									success: function(){
										response.success(0); // new owner was added and notified
										},
									error: function(error) {
										response.success(0); // new owner was added but couldn't be notified
										console.error("Error telling user " + newOwnerUsername + " he's a new owner for group: " + groupname + " Error : " + error); // If there is an error, it is logged
									}	
								});

							});

						} else {

							response.success(-2); // The requested user is already an owner of the group!

						}
					
					} else {
					
						response.success(-4); // The current user isn't an owner of the group!
					
					}

				}
			});

  		}

  	}

  },function(error) {
      response.error("Unable to add an owner (" + newOwnerUsername + ") for group " + groupname + "for user " + username);
      console.error("Error adding an owner (" + newOwnerUsername + ") for group " + groupname + "for user " + username + " Error : " + error); // If there is an error, it is logged
    });
	}
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
			"subscribersOnly": results[i].get("subscribersOnly"),
			"group": results[i].get("group"),
			"groupname": results[i].get("groupname"),
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


//// groupData

// Sending the data on a group to the user who made the request. The response depends on the status of the user (member or owner of the group)
Parse.Cloud.define("groupData", function(request, response) {
  Parse.Cloud.useMasterKey();
  var groupname = request.params.groupname;
  var username = request.params.username;
  
  var query = new Parse.Query("Group");
  query.equalTo("name", groupname);

  query.first().then(function(group){
  		
  		var owners = group.get("owners");
  		var members = group.get("members");
  		var isOwner = (owners.indexOf(username)!=-1);
  		var isMember = (members.indexOf(username)!=-1);

  		if (isOwner) {

	  		var answer = {
	  			"groupname" : group.get("name"),
	  			"members" : group.get("members"),
				"owners" : group.get("owners"),
	  			"createdAt": group.createdAt,
				"nbrMembers": group.get("nbrMembers"),
				"isOwner" : isOwner,
				"isMember" : isMember
	  		};

	  		response.success(answer);

  		} else if (isMember) {

	  		var answer = {
	  			"groupname" : group.get("name"),
				"owners" : group.get("owners"),
	  			"createdAt": group.createdAt,
				"nbrMembers": group.get("nbrMembers"),
				"isOwner" : isOwner,
				"isMember" : isMember
	  		};

	  		response.success(answer);

  		} else {

  			response.error("You are neither a member nor an owner of this group!");

  		}

  	},function(error){
  	response.error("Unale to access group data");
  	console.error("Error while accessing group data for group " + groupname + ". Error : " + error.code + " " + error.message); // If there is an error, it is logged;
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


//// groupExists

// Testing if a particular user exists (for subscription purposes)

Parse.Cloud.define("groupExists", function(request, response) {
  Parse.Cloud.useMasterKey();
  var query = new Parse.Query("Group");
  query.equalTo("name",request.params.groupname); // Querying the user whose username is in the parameters given by the app
  query.first().then(function(group) {
  	response.success((group!=null));
  },
	function(error) {
      response.error("Unable to look for group");
      console.error("Error looking for group " + request.params.groupname + " Error : " + error); // If there is an error, it is logged
    });
});

////


//// newQuestion

// Creating a new question
Parse.Cloud.define("newQuestion", function(request, response) {
  Parse.Cloud.useMasterKey();
  var asker = Parse.User.current();
  var asker_username = asker.get("username");
  var text = request.params.text;
  var nbrAnswers = request.params.nbrAnswers;
  var subscribersOnly = request.params.subscribersOnly;
  var group = request.params.group;
  var groupname = request.params.groupname;
  var nbrUsersTargeted = request.params.nbrUsersTargeted; // Getting the parameters set by the User in the app
  var timeToAnswer = 120; // timeToAnswer = 120s

  var newQuestion = new Parse.Object("Question");
		newQuestion.set("asker",asker); // The user who asked the question
		newQuestion.set("askerUsername",asker_username); // His/her username (so that the user getting the question doesn't have access to the asker's Id and other fields)
		newQuestion.set("text",text); // The question per say
		newQuestion.set("nbrAnswers",nbrAnswers); // Number of possible answers to the question
		newQuestion.set("timeToAnswer", timeToAnswer); // The time given to the user to answer the question
		if (subscribersOnly) {
			var channel = "User_" + asker_username;
		} else {
			var channel = "Group_" + groupname;
		}
		newQuestion.set("channel", channel); // The channel to which the question will be sent
		newQuestion.set("subscribersOnly", subscribersOnly); // Is it for subscribers only?
		newQuestion.set("group",group); // Is it for a specific group?
		newQuestion.set("groupname",groupname); // Name of the targeted group, "" if not a question for a group
		newQuestion.set("toSend",true); // If true, then the question hasn't been sent yet and needs to be sent to its channel
		newQuestion.set("published",false); // If true, then the results have been published, the question has finished its life cycle
		newQuestion.set("nbrUsersTargeted",nbrUsersTargeted); // The total number of users targeted by the question
		newQuestion.set("answeredBy",[]); // Users who answered this question
		newQuestion.set("answer1",request.params.answer1); // Text for the first possible answer
		newQuestion.set("answer2",request.params.answer2); // Text for the second possible answer
		newQuestion.set("nA",0); // Number of users who answered
		newQuestion.set("nA1",0); // Number of users who answered answer 1
		newQuestion.set("nA2",0); // Number of users who answered answer 2
		newQuestion.set("nA3",0); // Number of users who answered answer 3
		newQuestion.set("nA4",0); // Number of users who answered answer 4
		newQuestion.set("nA5",0); // Number of users who answered answer 5

  switch (nbrAnswers) { // 4 cases depending on the number of possible answers to the question
	case 2:
		newQuestion.set("answer3",""); 
		newQuestion.set("answer4","");
		newQuestion.set("answer5",""); // No third, fourth or fifth answers in this case
		newQuestion.save().then(function(){ // Saving the question
		response.success();
		}, function(error){
		console.error("Error while creating a quesion with 2 answers for asker " + asker_username+ ". Error : " + error.code + " " + error.message); // If there is an error, it is logged;
		});
	break;
	case 3:
		newQuestion.set("answer3",request.params.answer3);
		newQuestion.set("answer4","");
		newQuestion.set("answer5","");
		newQuestion.save().then(function(){
		response.success();
		}, function(error){
		console.error("Error while creating a quesion with 3 answers for asker " + asker_username+ ". Error : " + error.code + " " + error.message); // If there is an error, it is logged;
		});
	break;
	case 4:
		newQuestion.set("answer3",request.params.answer3);
		newQuestion.set("answer4",request.params.answer4);
		newQuestion.set("answer5","");
		newQuestion.save().then(function(){
		response.success();
		}, function(error){
		console.error("Error while creating a quesion with 4 answers for asker " + asker_username+ ". Error : " + error.code + " " + error.message); // If there is an error, it is logged;
		});
	break;
	case 5:
		newQuestion.set("answer3",request.params.answer3);
		newQuestion.set("answer4",request.params.answer4);
		newQuestion.set("answer5",request.params.answer5);
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
	var nA = request.object.get("nA");
	var subscribersOnly = request.object.get("subscribersOnly");
	var published = request.object.get("published");
	var nbrUsersTargeted = request.object.get("nbrUsersTargeted"); // Getting the required values from the question
	var minForR = Math.max(1,Math.floor((nbrUsersTargeted*50)/100)); // Calculating the minimum number of users who have to answer the question before the results are published: 50% of the targeted users
	request.object.set("minForResults",minForR);
	var now = new Date(); // Current date
	request.object.set("lastUpdate",now.getTime()); // Setting the date of the question's lastUpdate
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
	var time_for_answer = 3600000; // expiration time for notification requesting answers
	var time_for_results = 86400000; // expiration time for notification giving results
	
	var toSend = request.object.get("toSend");
	var toSendForResults = request.object.get("toSendForResults"); // Values set in question beforeSave to determine where the question is in its life cycle
	
	if (toSend) { // The question needs to be broadcast for answers
	
	var asker = request.object.get("asker");

	Parse.Push.send({
		channels: [request.object.get("channel")], // The channel targets all users of the chosen group or of the asker's subscribers
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
			group: request.object.get("group"),
			groupname: request.object.get("groupname"),
			subscribersOnly: request.object.get("subscribersOnly"),
			askerUsername: request.object.get("askerUsername"),
			timeToAnswer: request.object.get("timeToAnswer")
		},
		expiration_interval: time_for_answer
		}, {
		success: function(){
			request.object.set("toSend",false); // The life cycle advances: the question has been sent
			request.object.save();
			console.log("Notification sent!")
			},
		error: function(error) {
			console.error("Error sending question to subscribers or to group: questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
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
			console.error("Error telling asker the question has been sent (subscribers or group): questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
		}	
	});
	
	}

	if (toSendForResults) { // Enough users have answered, the results can be published to all the targeted users + the asker

	var asker = request.object.get("asker");

	Parse.Push.send({
		channels: [request.object.get("channel")],
		data: {
			action: "com.spersio.opinion.RESULTS",
			questionID: request.object.id,
			createdAt: request.object.createdAt,
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
			subscribersOnly: request.object.get("subscribersOnly"),
			group: request.object.get("group"),
			groupname: request.object.get("groupname"),
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
			console.error("Error sending the results to the asker's subscribers or to a group: questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
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
			subscribersOnly: request.object.get("subscribersOnly"),
			group: request.object.get("group"),
			groupname: request.object.get("groupname"),
			askerUsername: request.object.get("askerUsername")
		},
		expiration_interval: time_for_results
		}, {
		success: function(){
			},
		error: function(error) {
			console.error("Error sending the results to the asker (subscribersOnly or group): questionID = " + request.object.id + " Error : " + error); // If there is an error, it is logged
		}	
		});

	}

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

/*
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
});*/