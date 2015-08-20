'use strict';

/**
 * Module dependencies.
 */
var _ = require('lodash'),
	path = require('path'),
	mongoose = require('mongoose'),
	[model:name] = mongoose.model('[model:name]'),
	errorHandler = require(path.resolve('./modules/core/server/controllers/errors.server.controller'));

/**
 * Create a [model:name:lower]
 */
exports.create = function(req, res) {
	var [model:name:lower] = new [model:name](req.body);
	[model:name:lower].user = req.user;

	[model:name:lower].save(function(err) {
		if (err) {
			return res.status(400).send({
				message: errorHandler.getErrorMessage(err)
			});
		} else {
			res.json([model:name:lower]);
		}
	});
};

/**
 * Show the current [model:name:lower]
 */
exports.read = function(req, res) {
	res.json(req.[model:name:lower]);
};

/**
 * Update a [model:name:lower]
 */
exports.update = function(req, res) {
	var [model:name:lower] = req.[model:name:lower];

	//[model:name:lower].title = req.body.title;
	//[model:name:lower].content = req.body.content;

	//SS- Adding previous code to pick up the req body
	[model:name:lower] = _.extend([model:name:lower] , req.body);

	[model:name:lower].save(function(err) {
		if (err) {
			return res.status(400).send({
				message: errorHandler.getErrorMessage(err)
			});
		} else {
			res.json([model:name:lower]);
		}
	});
};

/**
 * Delete an [model:name:lower]
 */
exports.delete = function(req, res) {
	var [model:name:lower] = req.[model:name:lower];

	[model:name:lower].remove(function(err) {
		if (err) {
			return res.status(400).send({
				message: errorHandler.getErrorMessage(err)
			});
		} else {
			res.json([model:name:lower]);
		}
	});
};

/**
 * List of [model:name:plural]
 */
exports.list = function(req, res) {
	[model:name].find().sort('-created').populate('user', 'displayName').exec(function(err, [model:name:lower:plural]) {
		if (err) {
			return res.status(400).send({
				message: errorHandler.getErrorMessage(err)
			});
		} else {
			res.json([model:name:lower:plural]);
		}
	});
};

/**
 * [model:name] middleware
 */
exports.[model:name:lower]ByID = function(req, res, next, id) {
	[model:name].findById(id).populate('user', 'displayName').exec(function(err, [model:name:lower]) {
		if (err) return next(err);
		if (![model:name:lower]) return next(new Error('Failed to load [model:name:lower] ' + id));
		req.[model:name:lower] = [model:name:lower];
		next();
	});
};
