(function() {
    'use strict';

    angular
        .module('[model:name:lower:plural]')
        .factory('[model:name:plural]Form', factory);

    function factory() {

      var getFormFields = function(disabled) {

        var fields = [
  				{
					[properties]
  					key: [property:name:value:lower],
  					type: 'input',
  					templateOptions: {
					label: '[property:name:value]:',
					disabled: disabled
					[/properties]
  			    }
  				}

  			];

        return fields;

      };

      var service = {
        getFormFields: getFormFields
      };

      return service;

  }

})();
