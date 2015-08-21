'use strict';

describe('[model:name:plural] E2E Tests:', function() {
	describe('Test [model:name:lower:plural] page', function() {
		it('Should report missing credentials', function() {
			browser.get('http://localhost:3000/#!/[model:name:lower:plural]');
			expect(element.all(by.repeater('[model:name:lower] in [model:name:lower:plural]')).count()).toEqual(0);
		});
	});
});
