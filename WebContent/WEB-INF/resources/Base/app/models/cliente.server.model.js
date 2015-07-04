'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
	Schema = mongoose.Schema;

/**
 * Cliente Schema
 */
var ClienteSchema = new Schema({
	nombre: {
		type: String,
		default: '',
		required: 'Por favor ingrese nombre',
		trim: true

	},
	apellido: {
		type: String,
		default: '',
		required: 'Por favor ingrese apellido',
		trim: true
	},
	ciudad: {
		type: String,
		default: '',
		required: 'Por favor ingrese ciudad',
		trim: true
	},
	pais: {
		type: String,
		default: '',
		trim: true
	},
	email: {
		type: String,
		default: '',
		trim: true
	},
	telefono: {
		type: String,
		default: '',
		trim: true
	},
	referido: {
		type: Boolean
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

mongoose.model('Cliente', ClienteSchema);
