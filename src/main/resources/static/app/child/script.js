angular.module('healthyKids')
	.controller('child', ChildController);

ChildController.$inject = ['$routeParams', 'childService'];

function ChildController($routeParams, childService) {
	var self = this;

	self.displayInfoSection = displayInfoSection
	self.isDailyGoal = isDailyGoal;
	self.isWeeklyGoal = isWeeklyGoal;
	self.isSuperGoal = isSuperGoal;
	self.selectAchievement = selectAchievement;
	self.setSticker = setSticker;
	self.updateRewards = updateRewards;
	self.changeWeek = changeWeek;
	self.changeInfoTab = changeInfoTab;
	
	self.getDayObject = getDayObject;
	
	self.stickers = [];
	self.newCustomReward = '';
	self.infoOpen = false

	self.week = 'this';
	self.achievements = {};

	
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
	
	/**
	 * Change the week that we are viewing/editing.
	 * The view should always refer to this controller's 'achievements' object - this method
	 * will switch that to the correct week.
	 */
	function changeWeek(which) {
		self.week = which;
		if (which === 'this') {
			self.achievements = self.child.dailyAchievements;
		} else if (which === 'last') {
			self.achievements = self.child.lastWeekDailyAchievements;
		} 
		
	}

	function changeInfoTab(activity) {
		var color = ''
		switch(activity) {
		    case 'movement':
		        color = '#7251A0'
		        break;
		    case 'nutrition':
		        color = '#80C536'
		        break;
		    case 'sleep':
		        color = '#00B8B1'
		        break;
		}
		$('.info-container').css({'background-color': color})
		$('.info-tabs__behind-tabs > div:nth-child(2)').css({'background-color': color})
		$('.info-button').css({'background-color': color})
	}

	function displayInfoSection() {
		self.infoOpen = !self.infoOpen
		if (self.infoOpen) {
			$('.info-inner').show()
			$('.info-container').animate({height: '1000px'})
		} else {
			$('.info-inner').hide()
			$('.info-container').animate({height: '35px'})
		}
	}
	
	function selectAchievement(weekday, activity) {
		if (typeof self.child === 'undefined') {
			return;
		}
		activity = activity.value;
		var current = self.achievements[weekday.value][activity];
		self.achievements[weekday.value][activity] = !current;
		childService.setAchievement(self.child.id, self.achievements[weekday.value]);
	}
	
	function isDailyGoal(weekday) {
		if (typeof self.child === 'undefined') {
			return false;
		}
		var dailyGoals = self.achievements[weekday.value];
		return dailyGoals.sleep && dailyGoals.nutrition && dailyGoals.movement;
	}
	
	function isWeeklyGoal(activity) {
		if (typeof self.child === 'undefined') {
			return false;
		}
		var weeklyGoals = self.achievements;
		for (var key in weeklyGoals) {
		   if (weeklyGoals.hasOwnProperty(key)) {
			   if (!weeklyGoals[key][activity.value]) {
				   return false;
			   }
		   }
		}
		return true;
	}
	
	function isSuperGoal() {
		if (typeof self.child === 'undefined') {
			return false;
		}
		var isGoal = true;
		self.days.forEach(function(day) {
			if (!self.isDailyGoal(day)) {
				isGoal = false;
			}
		});
		return isGoal;
	}
	
	function getDayObject(day) {
		return self.days.filter(function(el) {
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
		if (self.newCustomReward !== '' && self.child.customRewards.length < 3) {
			self.child.customRewards.push({id:-1, reward:self.newCustomReward});
			self.newCustomReward = '';
		}
		
		childService.setRewards(self.child.id, self.child.customRewards).then(function(data) {
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
				self.achievements = self.child.dailyAchievements;
			});
		childService.getStickers()
		.then(function(data) {
			self.stickers = data;
		});
	
	}


}