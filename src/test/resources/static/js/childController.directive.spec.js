describe('Unit testing child controller', function() {
	var $compile,
	$rootScope, 
	$controller,
	scope;

	// Load the myApp module, which contains the directive
	beforeEach(module('healthyKids'));

	// Store references to $rootScope and $compile
	// so they are available to all tests in this describe block
	beforeEach(inject(function(_$compile_, _$controller_, _$rootScope_){
		// The injector unwraps the underscores (_) from around the parameter names when matching
		$compile = _$compile_;
		$rootScope = _$rootScope_;
		$controller = _$controller_;

		scope = $rootScope.$new();

		createController = function() {
			return $controller('child', {
				'$scope': scope
			});
		};
	}));

	it('should have a method to check if daily goals are met', function() {
		var controller = createController();

		var mondayObject = controller.getDayObject('Monday');
		console.log(mondayObject);
		expect(controller.isDailyGoal(mondayObject)).toBe(false);
		
		controller.child = { "dailyAchievements":{"0":{"sleep":false,"nutrition":true,"movement":true}}};
		expect(controller.isDailyGoal(mondayObject)).toBe(false);
		controller.child = { "dailyAchievements":{"0":{"sleep":true,"nutrition":true,"movement":true}}};
		expect(controller.isDailyGoal(mondayObject)).toBe(true);
		
		
	});
	
	it('should have a method to check if weekly goals are met', function() {
		var controller = createController();
		var testActivity = {'value':'movement'};
		
		expect(controller.isWeeklyGoal(testActivity)).toBe(false);
		
		controller.child = { "dailyAchievements":{"0":{"sleep":true,"nutrition":true,"movement":true}}};

		var achievements = {};
		for (var i = 0; i < 7; i++) {
			achievements[i] = {"sleep":true,"nutrition":false,"movement":true};
		}
		controller.child = { "dailyAchievements":achievements};
		expect(controller.isWeeklyGoal(testActivity)).toBe(true);
		expect(controller.isWeeklyGoal({'value':'nutrition'})).toBe(false);
		
	});
});