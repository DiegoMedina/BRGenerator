'use strict';

var should = require('should'),
	request = require('supertest'),
	app = require('../../server'),
	mongoose = require('mongoose'),
	User = mongoose.model('User'),
	Inmueble = mongoose.model('Inmueble'),
	agent = request.agent(app);

/**
 * Globals
 */
var credentials, user, inmueble;

/**
 * Inmueble routes tests
 */
describe('Inmueble CRUD tests', function() {
	beforeEach(function(done) {
		// Create user credentials
		credentials = {
			username: 'username',
			password: 'password'
		};

		// Create a new user
		user = new User({
			firstName: 'Full',
			lastName: 'Name',
			displayName: 'Full Name',
			email: 'test@test.com',
			username: credentials.username,
			password: credentials.password,
			provider: 'local'
		});

		// Save a user to the test db and create new Inmueble
		user.save(function() {
			inmueble = {
				name: 'Inmueble Name'
			};

			done();
		});
	});

	it('should be able to save Inmueble instance if logged in', function(done) {
		agent.post('/auth/signin')
			.send(credentials)
			.expect(200)
			.end(function(signinErr, signinRes) {
				// Handle signin error
				if (signinErr) done(signinErr);

				// Get the userId
				var userId = user.id;

				// Save a new Inmueble
				agent.post('/inmuebles')
					.send(inmueble)
					.expect(200)
					.end(function(inmuebleSaveErr, inmuebleSaveRes) {
						// Handle Inmueble save error
						if (inmuebleSaveErr) done(inmuebleSaveErr);

						// Get a list of Inmuebles
						agent.get('/inmuebles')
							.end(function(inmueblesGetErr, inmueblesGetRes) {
								// Handle Inmueble save error
								if (inmueblesGetErr) done(inmueblesGetErr);

								// Get Inmuebles list
								var inmuebles = inmueblesGetRes.body;

								// Set assertions
								(inmuebles[0].user._id).should.equal(userId);
								(inmuebles[0].name).should.match('Inmueble Name');

								// Call the assertion callback
								done();
							});
					});
			});
	});

	it('should not be able to save Inmueble instance if not logged in', function(done) {
		agent.post('/inmuebles')
			.send(inmueble)
			.expect(401)
			.end(function(inmuebleSaveErr, inmuebleSaveRes) {
				// Call the assertion callback
				done(inmuebleSaveErr);
			});
	});

	it('should not be able to save Inmueble instance if no name is provided', function(done) {
		// Invalidate name field
		inmueble.name = '';

		agent.post('/auth/signin')
			.send(credentials)
			.expect(200)
			.end(function(signinErr, signinRes) {
				// Handle signin error
				if (signinErr) done(signinErr);

				// Get the userId
				var userId = user.id;

				// Save a new Inmueble
				agent.post('/inmuebles')
					.send(inmueble)
					.expect(400)
					.end(function(inmuebleSaveErr, inmuebleSaveRes) {
						// Set message assertion
						(inmuebleSaveRes.body.message).should.match('Please fill Inmueble name');
						
						// Handle Inmueble save error
						done(inmuebleSaveErr);
					});
			});
	});

	it('should be able to update Inmueble instance if signed in', function(done) {
		agent.post('/auth/signin')
			.send(credentials)
			.expect(200)
			.end(function(signinErr, signinRes) {
				// Handle signin error
				if (signinErr) done(signinErr);

				// Get the userId
				var userId = user.id;

				// Save a new Inmueble
				agent.post('/inmuebles')
					.send(inmueble)
					.expect(200)
					.end(function(inmuebleSaveErr, inmuebleSaveRes) {
						// Handle Inmueble save error
						if (inmuebleSaveErr) done(inmuebleSaveErr);

						// Update Inmueble name
						inmueble.name = 'WHY YOU GOTTA BE SO MEAN?';

						// Update existing Inmueble
						agent.put('/inmuebles/' + inmuebleSaveRes.body._id)
							.send(inmueble)
							.expect(200)
							.end(function(inmuebleUpdateErr, inmuebleUpdateRes) {
								// Handle Inmueble update error
								if (inmuebleUpdateErr) done(inmuebleUpdateErr);

								// Set assertions
								(inmuebleUpdateRes.body._id).should.equal(inmuebleSaveRes.body._id);
								(inmuebleUpdateRes.body.name).should.match('WHY YOU GOTTA BE SO MEAN?');

								// Call the assertion callback
								done();
							});
					});
			});
	});

	it('should be able to get a list of Inmuebles if not signed in', function(done) {
		// Create new Inmueble model instance
		var inmuebleObj = new Inmueble(inmueble);

		// Save the Inmueble
		inmuebleObj.save(function() {
			// Request Inmuebles
			request(app).get('/inmuebles')
				.end(function(req, res) {
					// Set assertion
					res.body.should.be.an.Array.with.lengthOf(1);

					// Call the assertion callback
					done();
				});

		});
	});


	it('should be able to get a single Inmueble if not signed in', function(done) {
		// Create new Inmueble model instance
		var inmuebleObj = new Inmueble(inmueble);

		// Save the Inmueble
		inmuebleObj.save(function() {
			request(app).get('/inmuebles/' + inmuebleObj._id)
				.end(function(req, res) {
					// Set assertion
					res.body.should.be.an.Object.with.property('name', inmueble.name);

					// Call the assertion callback
					done();
				});
		});
	});

	it('should be able to delete Inmueble instance if signed in', function(done) {
		agent.post('/auth/signin')
			.send(credentials)
			.expect(200)
			.end(function(signinErr, signinRes) {
				// Handle signin error
				if (signinErr) done(signinErr);

				// Get the userId
				var userId = user.id;

				// Save a new Inmueble
				agent.post('/inmuebles')
					.send(inmueble)
					.expect(200)
					.end(function(inmuebleSaveErr, inmuebleSaveRes) {
						// Handle Inmueble save error
						if (inmuebleSaveErr) done(inmuebleSaveErr);

						// Delete existing Inmueble
						agent.delete('/inmuebles/' + inmuebleSaveRes.body._id)
							.send(inmueble)
							.expect(200)
							.end(function(inmuebleDeleteErr, inmuebleDeleteRes) {
								// Handle Inmueble error error
								if (inmuebleDeleteErr) done(inmuebleDeleteErr);

								// Set assertions
								(inmuebleDeleteRes.body._id).should.equal(inmuebleSaveRes.body._id);

								// Call the assertion callback
								done();
							});
					});
			});
	});

	it('should not be able to delete Inmueble instance if not signed in', function(done) {
		// Set Inmueble user 
		inmueble.user = user;

		// Create new Inmueble model instance
		var inmuebleObj = new Inmueble(inmueble);

		// Save the Inmueble
		inmuebleObj.save(function() {
			// Try deleting Inmueble
			request(app).delete('/inmuebles/' + inmuebleObj._id)
			.expect(401)
			.end(function(inmuebleDeleteErr, inmuebleDeleteRes) {
				// Set message assertion
				(inmuebleDeleteRes.body.message).should.match('User is not logged in');

				// Handle Inmueble error error
				done(inmuebleDeleteErr);
			});

		});
	});

	afterEach(function(done) {
		User.remove().exec();
		Inmueble.remove().exec();
		done();
	});
});