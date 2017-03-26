angular.module('healthyKids')
	.controller('child', ChildController);

ChildController.$inject = ['$routeParams', 'childService'];

function ChildController($routeParams, childService) {
	var self = this;

	self.isDailyGoal = isDailyGoal;
	self.isWeeklyGoal = isWeeklyGoal;
	self.selectAchievement = selectAchievement;
	
	self.days = [
		{"name":"Monday", "value":0},
		{"name":"Tuesday", "value":1},
		{"name":"Wednesday", "value":2},
		{"name":"Thursday", "value":3},
		{"name":"Friday", "value":4},
		{"name":"Saturday", "value":5},
		{"name":"Sunday", "value":6}
	];
	self.targets = [
		{'display':'Move', 'value':'movement'},
		{'display':'Eat', 'value':'nutrition'},
		{'display':'Sleep', 'value':'sleep'}
	];
	
	activate();
	
	function selectAchievement(weekday, activity) {
		if (typeof self.child === 'undefined') {
			return;
		}
		activity = activity.value;
		var current = self.child.dailyAchievements[weekday.value][activity];
		self.child.dailyAchievements[weekday.value][activity] = !current;
		childService.setAchievement(self.child.id, self.child.dailyAchievements[weekday.value]);
	}
	
	function isDailyGoal(weekday) {
		if (typeof self.child === 'undefined') {
			return false;
		}
		var dailyGoals = self.child.dailyAchievements[weekday.value];
		return dailyGoals.sleep && dailyGoals.nutrition && dailyGoals.movement;
	}
	
	function isWeeklyGoal(activity) {
		if (typeof self.child === 'undefined') {
			return false;
		}
		var weeklyGoals = self.child.dailyAchievements;
		for (var key in weeklyGoals) {
		   if (weeklyGoals.hasOwnProperty(key)) {
			   if (!weeklyGoals[key][activity.value]) {
				   return false;
			   }
		   }
		}
		return true;
	}
	
	function activate() {
		childService.getChildren().then(function(data) {
			self.children = data;
		});
		return childService.getChild($routeParams.id)
			.then(function(data) {
				self.child = data;
			});
		
	}


}