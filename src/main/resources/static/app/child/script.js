angular.module('healthyKids')
	.controller('child', ChildController);

ChildController.$inject = ['$routeParams', 'childService'];

function ChildController($routeParams, childService) {
	var self = this;

	self.isDailyGoal = isDailyGoal;
	self.isWeeklyGoal = isWeeklyGoal;
	self.selectAchievement = selectAchievement;
	self.setSticker = setSticker;
	self.updateRewards = updateRewards;
	
	self.getDayObject = getDayObject;
	
	self.stickers = [];
	self.newCustomReward = '';
	
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
	
	function getDayObject(day) {
		return self.days.filter(function(el) {
			console.log(el.name, day, el.name === day);
			return el.name === day;
		})[0];
	}
	
	function setSticker(sticker) {
		self.child.sticker = sticker;
		childService.setSticker(self.child.id, sticker);
		self.children.forEach(function(child) {
			if (child.id === self.child.id) {
				child.sticker = sticker;
			}
		});
	}
	
	function updateRewards() {
		//first update any that are already set
		console.log(self.child.customRewards);
		console.log(self.child.customRewards[0]);
		console.log(self.child.customRewards[1]);

		if (self.newCustomReward !== '' && self.child.customRewards.length < 3) {
			self.child.customRewards.push({id:-1, reward:self.newCustomReward});
			self.newCustomReward = '';
		}
		
		childService.setRewards(self.child.id, self.child.customRewards).then(function(data) {
			console.log(data);
			self.child.customRewards = data.customRewards;
		});

	}
	
	function activate() {
		childService.getChildren().then(function(data) {
			self.children = data;
		});
		childService.getChild($routeParams.id)
			.then(function(data) {
				self.child = data;
			});
		childService.getStickers()
		.then(function(data) {
			self.stickers = data;
		});
	
	}


}