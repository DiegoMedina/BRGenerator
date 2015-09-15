'use strict';

/**
 * Module dependencies.
 */
var [model:name:lower:plural]Policy = require('../policies/[model:name:lower:plural].server.policy.js'),
	[model:name:lower:plural] = require('../controllers/[model:name:lower:plural].server.controller.js');

module.exports = function(app) {
	// [model:name:plural] collection routes
	app.route('/api/[model:name:lower:plural]').all([model:name:lower:plural]Policy.isAllowed)
		.get([model:name:lower:plural].list)
		.post([model:name:lower:plural].create);

	// Single [model:name:lower] routes
	app.route('/api/[model:name:lower:plural]/:[model:name:lower]Id').all([model:name:lower:plural]Policy.isAllowed)
		.get([model:name:lower:plural].read)
		.put([model:name:lower:plural].update)
		.delete([model:name:lower:plural].delete);

	// Finish by binding the [model:name:lower] middleware
	app.param('[model:name:lower]Id', [model:name:lower:plural].[model:name:lower]ByID);
};
