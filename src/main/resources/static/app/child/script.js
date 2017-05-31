angular.module('healthyKids')
	.controller('child', ChildController);

ChildController.$inject = ['$routeParams', 'childService', 'accountService', 'goalService', 'rewardService', '$location'];

function ChildController($routeParams, childService, accountService, goalService, rewardService,  $location) {
	var self = this;

	self.displayInfoSection = displayInfoSection
	self.displayStickerSelect = displayStickerSelect
	self.displayCongrats = displayCongrats
	self.getStickers = getStickers
	self.isDailyGoal = isDailyGoal;
	self.isWeeklyGoal = isWeeklyGoal;
	self.isSuperGoal = isSuperGoal;
	self.selectAchievement = selectAchievement;
	self.setSticker = setSticker;
	self.updateRewards = updateRewards;
	self.changeWeek = changeWeek;
	self.changeInfoTab = changeInfoTab;
	self.randomTrophy = randomTrophy;
	self.randomStar = randomStar;
	self.facebookShare = facebookShare;


	
	/**
	 * Goals
	 * setGoals: function to set whether to use custom or default goals
	 * goals: object holding default values
	 * customGoals: object holding custom values
	 * updateGoals: function to persist changes to database
	 */
	self.setGoals = setGoals;
	self.defaultGoals = goalService.predefinedGoals;//{nutrition:"I will eat three sorts of vegetables at dinner time.", movement:"I will go for a walk or a bike ride", sleep:"I will start quiet time one hour before bedtime"};
	self.customGoals = {nutrition:"", movement:"", sleep:""}
	self.useCustomGoals = false;
	self.updateDefaultGoals = updateDefaultGoals;
	self.selectedGoal = selectedGoal;
	self.updateGoals = updateGoals;
	
	self.updateDefaultRewards = updateDefaultRewards;
	self.defaultRewards = rewardService.predefinedRewards;
	
	
	self.addGoal = addGoal;
	self.saveGoal = saveGoal;
	self.editGoal = editGoal;
	self.editingGoal = {id:-1, target:'MOVEMENT'};
	self.selectedGoalId = selectedGoalId;
	self.viewGoalsScreen = viewGoalsScreen;
	self.getSummary = getSummary;
	self.getTotalSummary = getTotalSummary;
	self.selectHeaderButton = selectHeaderButton;
	self.scrollTop = scrollTop;
	
	self.deleteChild = deleteChild;
	
	self.getDayObject = getDayObject;
	
	self.stickers = [];
	self.newCustomReward = '';
	self.infoOpen = false
	self.congratsOpen = false
	self.showDisclaimer = false;
	self.currentSticker = 'sticker1.png'

	self.week = 'this';
	self.achievements = {};
	self.current_actvity

	self.newChild = {};
	
	self.showAddGoalModal = false;

	self.createChild = createChild;
	self.logout = logout;

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
		{'display':'Eat', 'value':'nutrition'},
		{'display':'Move', 'value':'movement'},
		{'display':'Sleep', 'value':'sleep'}
	];
	
	activate();
	
	/**
	 * Change the week that we are viewing/editing.
	 * The view should always refer to this controller's 'achievements' object - this method
	 * will switch that to the correct week.
	 */

	function logout() {
		accountService.logout()
	}

	function changeWeek(which) {
		self.week = which;
		if (which === 'this') {
			self.achievements = self.child.dailyAchievements;
		} else if (which === 'last') {
			self.achievements = self.child.lastWeekDailyAchievements;
		} 	
	}

	function scrollTop() {
		window.scrollTo(0, 0);
	}

	function selectHeaderButton() {
		$('.header-button--' + self.currentPage).addClass('header-button--selected')
		$('.header-button--' + self.currentPage).append("<div class='header-button__selected-bg'></div>")
	}

	function randomTrophy() {
		var trophies = ['Trophies-1.png', 'Trophies-2.png', 'Trophies-3.png', 'Trophies-4.png']
		return trophies[Math.floor(Math.random() * trophies.length)]
	}

	function randomStar() {
		var stars = ['Stars-1.png', 'Stars-2.png', 'Stars-3.png', 'Stars-4.png', 'Stars-5.png']
		return stars[Math.floor(Math.random() * stars.length)]
	}
	
	function changeInfoTab(activity) {
		self.current_activity = activity
		var color = ''
		switch(activity.value) {
		    case 'movement':
		        color = '#7352A2'
		        break;
		    case 'nutrition':
		        color = '#82C341'
		        break;
		    case 'sleep':
		        color = '#00B8B0'
		        break;
		}
		$('.info-container').css({'background-color': color})
		$('.info-container h2, .info-container__text').css({'color': color})
		$('.info-tabs__behind-tabs > div:nth-child(2)').css({'background-color': color})
		$('.info-button').css({'background-color': color})
	}

	function displayInfoSection() {
		self.infoOpen = !self.infoOpen
		if (self.infoOpen) {
			$('.info-inner').show()
			$('.info-container').animate({height: '1150px'})
		} else {
			$('.info-inner').hide()
			// $('.info-container, .info-button').css({'background-color': '#FFCB05'})
			$('.info-container').animate({height: '35px'})
		}
	}

	function displayCongrats(which) {
		self.congratsOpen = !self.congratsOpen;
		if (self.congratsOpen) {
			$('.congrats-popup').fadeIn();
			self.randomTrophy()
			self.congratulations = which;
		} else {
			$('.congrats-popup').fadeOut();
		}
	}

	function getStickers() {
		childService.getStickers()
		.then(function(data) {
			self.stickers = data;
		});
	}

	function displayStickerSelect() { // Refactor into general fn
		self.stickerSelect = !self.stickerSelect
		if (self.stickerSelect) {
			$('.sticker-select__sticker').show()
			$('.sticker-select').animate({height: '250px'})
		} else {
			$('.sticker-select__sticker').hide()
			$('.sticker-select').animate({height: '0px'})
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
		if (self.isSuperGoal()) {
			displayCongrats('super');
		} else if (self.isWeeklyGoal({'value':activity})) {
			displayCongrats('weekly')
		} else if (self.isDailyGoal(weekday)) {
			displayCongrats('daily');
		}
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
		self.currentSticker = sticker
		self.newChild.sticker = sticker;
		// childService.setSticker(self.child.id, sticker);
		// self.children.forEach(function(child) {
		// 	if (child.id === self.child.id) {
		// 		child.sticker = sticker;
		// 	}
		// });
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
	
	function getSummary(activity) {
		if (typeof self.summary === 'undefined') {
			return;
		}
		if (activity.value === 'movement') {
			return self.summary.totalMovement;
		} else if (activity.value === 'sleep') {
			return self.summary.totalSleep;
		} else if (activity.value === 'nutrition') {
			return self.summary.totalNutrition;
		}
	}
	function getTotalSummary() {
		if (typeof self.summary === 'undefined') {
			return;
		}
		return self.summary.totalMovement + self.summary.totalSleep + self.summary.totalNutrition;
	}
	
	/**
	 * Open a modal dialog to 
	 */
	function addGoal() {
		self.editingGoal = {id:-1,target:'MOVEMENT'};
		self.showAddGoalModal = true;
	}

	function updateDefaultGoals() {
		childService.updateChild(self.child);
	}

	function updateDefaultRewards() {
		childService.updateChild(self.child);
	}

	
	function setGoals(which) {
		if (which == 'custom') {
			self.useCustomGoals = true;
		} else {
			self.useCustomGoals = false
		}
		updateGoals();
	}
	
	function saveGoal() {
		self.showAddGoalModal = false;
		childService.setGoal(self.child.id, self.editingGoal).then(function(data) {
			if (self.editingGoal.id < 0) {
				self.child.customGoals.push(data);
			} else {
				var goal = self.child.customGoals.find(function(goal) {return goal.id == self.editingGoal.id});
				goal.target = self.editingGoal.target;
				goal.selected = self.editingGoal.selected;
				goal.goal = self.editingGoal.goal;
			}
		});
	}
	
	function selectedGoal(target) {
		if (self.child == null) {
			return '';
		}
		if (self.useCustomGoals) {
			return self.customGoals[target].goal;
		} else {
			var selectedIndex = 1;
			if (target === 'nutrition') {
				selectedIndex = self.child.defaultNutritionGoal;
			} else if (target === 'movement') {
				selectedIndex = self.child.defaultMovementGoal;
			} else if (target === 'sleep') {
				selectedIndex = self.child.defaultSleepGoal;
			}
			var goal = self.defaultGoals[target].find(function(goal) {return goal.id == selectedIndex});
			return goal.goal;
		}
	}
	
	function editGoal(goal) {
		var edit = self.editingGoal;
		edit.id= goal.id;
		edit.target = goal.target;
		edit.goal = goal.goal;
		edit.selected = goal.selected;
		self.showAddGoalModal = true;
	}
	
	/**
	 * Set the route to the goals page
	 */
	function viewGoalsScreen() {
		$location.path('/child/'+self.child.id+'/goals');
	}
	
/*	function selectedGoal(target) {
		if (self.child == null) {
			return '';
		}
		var selectedGoal = self.child.customGoals.find(function(goal) {return goal.target == target && goal.selected});
		if (selectedGoal == null) {
			return getDefaultGoal(target);
		} else {
			return selectedGoal.goal;
		}
	}
*/	
	/**
	 * Return the id of the selected goal for this target, or -1 if there is not one (ie. to use the default).
	 */
	function selectedGoalId(target) {
		if (self.child == null) {
			return -1;
		}
		var selectedGoal = self.child.customGoals.find(function(goal) {return goal.target == target && goal.selected});
		if (selectedGoal == null) {
			return -1;
		} else {
			return selectedGoal.id;
		}
	}
	
	function getDefaultGoal(target) {
		//TODO move this into a config file somewhere, rather than hardcoding!
		if (target === 'MOVEMENT') {
			return 'Do 12 star jumps';
		} else if (target === 'NUTRITION') {
			return 'Eat 5 pieces of apple';
		} else if (target === 'SLEEP') {
			return 'Be in bed by 7pm';
		} else {
			return 'Unknown target '+target;
		}
	}
	
	/**
	 * Triggered on blur from editing goals.  Propagates the edited goals to the server
	 */
	function updateGoals() {
		var goals = self.customGoals;
		
		self.targets.forEach(function(target) {
			goals[target.value].selected = self.useCustomGoals;
			
			childService.setGoal(self.child.id, goals[target.value]).then(function(data) {
				if (self.editingGoal.id < 0) {
					goals[target.value] = data;
				} 
			});
		});
		
		
	}

	
	function getCustomGoal(target) {
		if (self.child == null) {
			return {id:-1, target:target.toUpperCase(), selected: false, goal:""}
		}
		var customGoal = goals.find(function(goal) {return goal.target == target.toUpperCase()});
		if (typeof(customGoal) === 'undefined') {
			customGoal = {id:-1, target:target.toUpperCase(), selected: self.customGoal, goal:""};
		} 
		return customGoal;
	} 

	function createChild() {
		childService.updateChild(self.newChild).then(function(data) {
			$location.path('/child-select');
		});
	}
	
	function activate() {
		childService.getChildren().then(function(data) {
			self.children = data;
		});
		if (!$routeParams.id) {
			return;
		}
		childService.getChild($routeParams.id)
			.then(function(data) {
				self.child = data;
				self.newChild = self.child;
				self.achievements = self.child.dailyAchievements;
				
				//Load custom goals into lookup map
				self.customGoals = {};
				self.targets.forEach(function(target) {
					var customGoal = self.child.customGoals.find(function(goal) {return goal.target == target.value.toUpperCase()});
					if (typeof(customGoal) === 'undefined') {
						customGoal = {id:-1, target:target.value.toUpperCase(), goal:""};
					} 
					self.customGoals[target.value] = customGoal;
					if (self.customGoals[target.value].selected) {
						self.useCustomGoals = true;
					}
				});
				if (self.child.defaultNutritionGoal == null) {
					self.child.defaultNutritionGoal = 1;
				}
				if (self.child.defaultSleepGoal == null) {
					self.child.defaultSleepGoal = 1;
				}
				if (self.child.defaultMovementGoal == null) {
					self.child.defaultMovementGoal = 1;
				}
				if (self.child.defaultNutritionReward == null) {
					self.child.defaultNutritionReward = 1;
				}
			});

		childService.getStickers()
		.then(function(data) {
			self.stickers = data;
		});

		childService.getSummary({id:$routeParams.id})
			.then(function(data) {
				self.summary = data;
			});
	}
	
	function deleteChild(id) {
		//NOTE WELL, assumes all relevant checks have been done, not reversible!
		childService.deleteChild({id:id}).then(function(data) {
			self.children = data;
			$location.path('/child-select/');
		});
	}

	function facebookShare() {
		FB.ui(
		 {
		  method: 'share',
		  href: 'https://pure-island-83329.herokuapp.com'
		}, function(response){});
	}


}