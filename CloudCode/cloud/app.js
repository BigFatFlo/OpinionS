
var express = require('express');
  var parseExpressHttpsRedirect = require('parse-express-https-redirect');
  var parseExpressCookieSession = require('parse-express-cookie-session');
  var app = express();

  app.set('views', 'cloud/views');
  app.set('view engine', 'ejs');
  app.use(parseExpressHttpsRedirect());  // Require user to be on HTTPS.
  app.use(express.bodyParser());
  app.use(express.cookieParser('YOUR_SIGNING_SECRET'));
  app.use(parseExpressCookieSession({ cookie: { maxAge: 3600000 } }));

// You could have a "Log In" link on your website pointing to this.
  app.get('/login', function(req, res) {
    // Renders the login form asking for username and password.
    res.render('login');
  });

  // Clicking submit on the login form triggers this.
  app.post('/login', function(req, res) {
    Parse.User.logIn(req.body.username, req.body.password).then(function() {
      // Login succeeded, redirect to homepage.
      // parseExpressCookieSession will automatically set cookie.
      res.redirect('/results/'+req.body.username);
    },
    function(error) {
      // Login failed, redirect back to login form.
      res.redirect('/login');
    });
  });

  // You could have a "Log Out" link on your website pointing to this.
  app.get('/logout', function(req, res) {
    Parse.User.logOut();
    res.redirect('/');
  });

  // The homepage renders differently depending on whether user is logged in.
  app.get('/', function(req, res) {
    //if (Parse.User.current()) {
      // No need to fetch the current user for querying Note objects.
      // var Note = Parse.Object.extend("Note");
      // var query = new Parse.Query(Note);
      // query.find().then(function(results) {
        // Render the notes that the current user is allowed to see.
       //},
      //function(error) {
        // Render error page.
      //});
    //} else {
    	res.render("homepage");
      // Render a public welcome page, with a link to the '/login' endpoint.
    //}
  });

  /*// You could have a "Profile" link on your website pointing to this.
  app.get('/profile', function(req, res) {
    // Display the user profile if user is logged in.
    if (Parse.User.current()) {
      // We need to fetch because we need to show fields on the user object.
      Parse.User.current().fetch().then(function(user) {
        // Render the user profile information (e.g. email, phone, etc).
      },
      function(error) {
        // Render error page.
      });
    } else {
      // User not logged in, redirect to login form.
      res.redirect('/login');
    }
  });*/
	
	app.get("/results/:questionId", function(req, res) {
		/*if (Parse.User.current()) {
			res.render("results", {username: req.params.username});
		} else {
			res.render("results", {username: "Log In failed!"});
		}*/

      var query = new Parse.Query("Question");

      query.get(req.params.questionId, {
      
      success: function(question) {

      res.render("results", {
        askerUsername: question.get("askerUsername"),
        date: question.createdAt, 
        country: question.get("country"),
        questionText: question.get("text"),
        nbrAnswers: question.get("nbrAnswers"),
        nA: question.get("nA"),
        nA1: question.get("nA1"),
        nA2: question.get("nA2"),
        nA3: question.get("nA3"),
        nA4: question.get("nA4"),
        nA5: question.get("nA5"),
        pcA1: question.get("pcA1"),
        pcA2: question.get("pcA2"),
        pcA3: question.get("pcA3"),
        pcA4: question.get("pcA4"),
        pcA5: question.get("pcA5"),
        answer1: question.get("answer1"),
        answer2: question.get("answer2"),
        answer3: question.get("answer3"),
        answer4: question.get("answer4"),
        answer5: question.get("answer5"),
        international: question.get("international"),
        around: question.get("around"),
        radius: question.get("radius"),
        subscribersOnly: question.get("subscribersOnly")
      });

      },
      error: function(){
      res.render("invalidId");
      }
      });  




	});

  app.listen();
