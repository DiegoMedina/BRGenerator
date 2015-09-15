'use strict';

var should = require('should'),
	request = require('supertest'),
	path = require('path'),
	mongoose = require('mongoose'),
	User = mongoose.model('User'),
	[model:name] = mongoose.model('[model:name]'),
	express = require(path.resolve('./config/lib/express'));

/**
 * Globals
 */
var app, agent, credentials, user, [model:name:lower];

/**
 * [model:name] routes tests
 */
describe('[model:name] CRUD tests', function() {
	before(function(done) {
		// Get application
		app = express.init(mongoose);
		agent = request.agent(app);

		done();
	});

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

		// Save a user to the test db and create new [model:name:lower]
		user.save(function() {
			[model:name:lower] = {
				title: '[model:name] Title',
				content: '[model:name] Content'
			};

			done();
		});
	});

	it('should be able to save an [model:name:lower] if logged in', function(done) {
		agent.post('/api/auth/signin')
			.send(credentials)
			.expect(200)
			.end(function(signinErr, signinRes) {
				// Handle signin error
				if (signinErr) done(signinErr);

				// Get the userId
				var userId = user.id;

				// Save a new [model:name:lower]
				agent.post('/api/[model:name:lower:plural]')
					.send([model:name:lower])
					.expect(200)
					.end(function([model:name:lower]SaveErr, [model:name:lower]SaveRes) {
						// Handle [model:name:lower] save error
						if ([model:name:lower]SaveErr) done([model:name:lower]SaveErr);

						// Get a list of [model:name:lower:plural]
						agent.get('/api/[model:name:lower:plural]')
							.end(function([model:name:lower:plural]GetErr, [model:name:lower:plural]GetRes) {
								// Handle [model:name:lower] save error
								if ([model:name:lower:plural]GetErr) done([model:name:lower:plural]GetErr);

								// Get [model:name:lower:plural] list
								var [model:name:lower:plural] = [model:name:lower:plural]GetRes.body;

								// Set assertions
								([model:name:lower:plural][0].user._id).should.equal(userId);
								([model:name:lower:plural][0].title).should.match('[model:name] Title');

								// Call the assertion callback
								done();
							});
					});
			});
	});

	it('should not be able to save an [model:name:lower] if not logged in', function(done) {
		agent.post('/api/[model:name:lower:plural]')
			.send([model:name:lower])
			.expect(403)
			.end(function([model:name:lower]SaveErr, [model:name:lower]SaveRes) {
				// Call the assertion callback
				done([model:name:lower]SaveErr);
			});
	});

	it('should not be able to save an [model:name:lower] if no title is provided', function(done) {
		// Invalidate title field
		[model:name:lower].title = '';

		agent.post('/api/auth/signin')
			.send(credentials)
			.expect(200)
			.end(function(signinErr, signinRes) {
				// Handle signin error
				if (signinErr) done(signinErr);

				// Get the userId
				var userId = user.id;

				// Save a new [model:name:lower]
				agent.post('/api/[model:name:lower:plural]')
					.send([model:name:lower])
					.expect(400)
					.end(function([model:name:lower]SaveErr, [model:name:lower]SaveRes) {
						// Set message assertion
						([model:name:lower]SaveRes.body.message).should.match('Title cannot be blank');

						// Handle [model:name:lower] save error
						done([model:name:lower]SaveErr);
					});
			});
	});

	it('should be able to update an [model:name:lower] if signed in', function(done) {
		agent.post('/api/auth/signin')
			.send(credentials)
			.expect(200)
			.end(function(signinErr, signinRes) {
				// Handle signin error
				if (signinErr) done(signinErr);

				// Get the userId
				var userId = user.id;

				// Save a new [model:name:lower]
				agent.post('/api/[model:name:lower:plural]')
					.send([model:name:lower])
					.expect(200)
					.end(function([model:name:lower]SaveErr, [model:name:lower]SaveRes) {
						// Handle [model:name:lower] save error
						if ([model:name:lower]SaveErr) done([model:name:lower]SaveErr);

						// Update [model:name:lower] title
						[model:name:lower].title = 'WHY YOU GOTTA BE SO MEAN?';

						// Update an existing [model:name:lower]
						agent.put('/api/[model:name:lower:plural]/' + [model:name:lower]SaveRes.body._id)
							.send([model:name:lower])
							.expect(200)
							.end(function([model:name:lower]UpdateErr, [model:name:lower]UpdateRes) {
								// Handle [model:name:lower] update error
								if ([model:name:lower]UpdateErr) done([model:name:lower]UpdateErr);

								// Set assertions
								([model:name:lower]UpdateRes.body._id).should.equal([model:name:lower]SaveRes.body._id);
								([model:name:lower]UpdateRes.body.title).should.match('WHY YOU GOTTA BE SO MEAN?');

								// Call the assertion callback
								done();
							});
					});
			});
	});

	it('should be able to get a list of [model:name:lower:plural] if not signed in', function(done) {
		// Create new [model:name:lower] model instance
		var [model:name:lower]Obj = new [model:name]([model:name:lower]);

		// Save the [model:name:lower]
		[model:name:lower]Obj.save(function() {
			// Request [model:name:lower:plural]
			request(app).get('/api/[model:name:lower:plural]')
				.end(function(req, res) {
					// Set assertion
					res.body.should.be.an.Array.with.lengthOf(1);

					// Call the assertion callback
					done();
				});

		});
	});


	it('should be able to get a single [model:name:lower] if not signed in', function(done) {
		// Create new [model:name:lower] model instance
		var [model:name:lower]Obj = new [model:name]([model:name:lower]);

		// Save the [model:name:lower]
		[model:name:lower]Obj.save(function() {
			request(app).get('/api/[model:name:lower:plural]/' + [model:name:lower]Obj._id)
				.end(function(req, res) {
					// Set assertion
					res.body.should.be.an.Object.with.property('title', [model:name:lower].title);

					// Call the assertion callback
					done();
				});
		});
	});

	it('should be able to delete an [model:name:lower] if signed in', function(done) {
		agent.post('/api/auth/signin')
			.send(credentials)
			.expect(200)
			.end(function(signinErr, signinRes) {
				// Handle signin error
				if (signinErr) done(signinErr);

				// Get the userId
				var userId = user.id;

				// Save a new [model:name:lower]
				agent.post('/api/[model:name:lower:plural]')
					.send([model:name:lower])
					.expect(200)
					.end(function([model:name:lower]SaveErr, [model:name:lower]SaveRes) {
						// Handle [model:name:lower] save error
						if ([model:name:lower]SaveErr) done([model:name:lower]SaveErr);

						// Delete an existing [model:name:lower]
						agent.delete('/api/[model:name:lower:plural]/' + [model:name:lower]SaveRes.body._id)
							.send([model:name:lower])
							.expect(200)
							.end(function([model:name:lower]DeleteErr, [model:name:lower]DeleteRes) {
								// Handle [model:name:lower] error error
								if ([model:name:lower]DeleteErr) done([model:name:lower]DeleteErr);

								// Set assertions
								([model:name:lower]DeleteRes.body._id).should.equal([model:name:lower]SaveRes.body._id);

								// Call the assertion callback
								done();
							});
					});
			});
	});

	it('should not be able to delete an [model:name:lower] if not signed in', function(done) {
		// Set [model:name:lower] user 
		[model:name:lower].user = user;

		// Create new [model:name:lower] model instance
		var [model:name:lower]Obj = new [model:name]([model:name:lower]);

		// Save the [model:name:lower]
		[model:name:lower]Obj.save(function() {
			// Try deleting [model:name:lower]
			request(app).delete('/api/[model:name:lower:plural]/' + [model:name:lower]Obj._id)
			.expect(403)
			.end(function([model:name:lower]DeleteErr, [model:name:lower]DeleteRes) {
				// Set message assertion
				([model:name:lower]DeleteRes.body.message).should.match('User is not authorized');

				// Handle [model:name:lower] error error
				done([model:name:lower]DeleteErr);
			});

		});
	});

	afterEach(function(done) {
		User.remove().exec();
		[model:name].remove().exec();
		done();
	});
});
