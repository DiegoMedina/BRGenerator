'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
	Schema = mongoose.Schema;

/**
 * Inmueble Schema
 */
var InmuebleSchema = new Schema({
	name: {
		type: String,
		default: '',
		required: 'Please fill Inmueble name',
		trim: true
	},
	calle: {
		type: String,
		default: '',
		required: 'Por favor ingrese calle',
		trim: true
	},
	numero: {
		type: String,
		default: '',
		required: 'Por favor ingrese numero',
		trim: true
	},
	created: {
		type: Date,
		default: Date.now
	},
	user: {
		type: Schema.ObjectId,
		ref: 'User'
	}
});

mongoose.model('Inmueble', InmuebleSchema);