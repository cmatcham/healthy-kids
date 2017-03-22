angular.module('healthyKids', [ 'ngRoute' , 'ngCookies' ])
.config(function($routeProvider, $httpProvider) {

	console.log('configging this is where I would check the cookie state');

	$routeProvider.when('/', {
		templateUrl : 'home.html',
		controller : 'home',
		controllerAs: 'controller'
	}).when('/login', {
		templateUrl : 'login.html',
		controller : 'navigation',
		controllerAs: 'controller'
	}).when('/child/:id' , {
		templateUrl : 'details.html',
		controller : 'child',
		controllerAs: 'controller'
	}).when('/progress' , {
		templateUrl : 'progress.html',
		controller : 'child',
		controllerAs: 'controller'
	}).otherwise('/');

	$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

	$httpProvider.interceptors.push(function($q, $location, $cookies, $rootScope) {
		return {
			'responseError': function(response) {
				console.log('intercepted a bad response, with status ', response.status);
				if (response.status === 401 || response.status === 500){			
					$cookies.remove('token');
					$location.path("/");
					$rootScope.authenticated = false;
				}
				return $q.reject(response);
		}
	}});

})
.run(function($rootScope, $http, $cookies, $location) {
	console.log('application running');

	//check if authenticated;
	var token = $cookies.get('token');

	if (token != null) {
		$http.defaults.headers.common.token = 'Bearer '+token;
		$rootScope.authenticated = true;
	} else {
		$rootScope.authenticated = false;
	}
	
	

	//todo put in an http interceptor to redirect to login if token invalid

}) 
.controller('home', function($http, $rootScope, $cookies, $location) {
	var self = this;
	
	if ($rootScope.authenticated) {
		$http.get('api/child/all').then(function(response) {
			self.children = response.data;
		});
	} else {
		self.children = [];
	}
	
	self.newChild = {};
	self.newAccount = {};

	self.createChild = createChild;
	self.createAccount = createAccount;

	function createChild() {
		//TODO get a real dob!
		$http.put('api/child', {firstName:self.newChild.firstName, lastName:self.newChild.lastName, dateOfBirth:'01/01/2000'}).then(function(response) {
			self.children.push(response.data);
		}, function(error) {
			console.log(error);
			console.log('error');
		});
	};

	function createAccount() {
		$http.post('account', {email:self.newAccount.email, password:self.newAccount.password}).then(function(response) {
			var token = response.data.token;
			$rootScope.authenticated = true;
			$cookies.put('token', token);
			$http.defaults.headers.common.token = 'Bearer '+token;
			self.error = null;
			$location.path("/");
		}, function(error) {
			if (error.status === 409) {
				self.error = 'An account with that email already exists';
			} else {
				self.error = "Error: "+error.data.message;
			}
		});
	};

})
.controller('child', function($rootScope, $http, $location, $routeParams) {
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
	
	function selectAchievement(weekday, activity) {
		if (typeof self.child === 'undefined') {
			return;
		}
		activity = activity.value;
		var current = self.child.dailyAchievements[weekday.value][activity];
		self.child.dailyAchievements[weekday.value][activity] = !current;
		$http.post('api/child/'+$routeParams.id+'/target', self.child.dailyAchievements[weekday.value]).then(function(response) {
		}, function(error) {
		});
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
	
	$http.get('api/child/'+$routeParams.id).then(function(response) {
		console.log(response);
		if (response.data === "") {
			$location.path("/");
		} else {
			self.child = response.data;
		}
	}, function(error) {
		console.log(error);
	});
})
.directive('target', function() {
	return {
		templateUrl: 'target.html'
	};
})
.controller('navigation', function($rootScope, $http, $location, $cookies) {

	var self = this
	self.active = true

	var authenticate = function(credentials, callback) {
		$http({
			method:'POST',
			url: '/login',
			headers: {'Content-Type': 'application/x-www-form-urlencoded'},
			data: $.param(credentials)

		}).then(function(response) {
			$rootScope.authenticated = true;
			$cookies.put('token', response.data);
			$http.defaults.headers.common.token = 'Bearer '+response.data;
			$location.path("/");
		}, function() {
			$rootScope.authenticated = false;
			callback && callback();
		});

	}

	//////////
	//////////
	//////////
	//////////

	$rootScope.showChildMenu = function() {
		self.active = !self.active
		if (self.active) {
			$('.child-menu').hide()
		} else {
			$('.child-menu').show()
		}
	}

	$rootScope.animateDayButton = function(activity, day) {
		activity = activity.toLowerCase();
		day = day.toLowerCase();
		console.log(activity, day);
		switch(activity) {
		    case 'move':
		        $('.tick-chart__column--' + activity + ' .tick-chart__day--' + day).css({'background-color': 'black'})
		        $('.tick-chart__column--' + activity + ' .tick-chart__day--' + day).addClass('.tick-chart__day--active')
		        break;
		    case 'eat':
		        $('.tick-chart__column--' + activity + ' .tick-chart__day--' + day).css({'background-color': 'black'})
		        $('.tick-chart__column--' + activity + ' .tick-chart__day--' + day).addClass('.tick-chart__day--active')
		        break;
		    case 'sleep':
		        $('.tick-chart__column--' + activity + ' .tick-chart__day--' + day).css({'background-color': 'black'})
		        $('.tick-chart__column--' + activity + ' .tick-chart__day--' + day).addClass('.tick-chart__day--active')
			    break;
		}
	}

	$rootScope.calculateDailyGoal = function() {
		for (var i = $('.tick-chart__day').length - 1; i >= 0; i--) {
			if ($($('.tick-chart__day')[i]).hasClass('.tick-chart__day--active')) {
				if ($($('.tick-chart__day')[i + 7]).hasClass('.tick-chart__day--active') && $($('.tick-chart__day')[i + 14]).hasClass('.tick-chart__day--active')) {
					var day = $($('.tick-chart__day')[i])[0].attributes['data-day'].value.toLowerCase()
					$('.daily-goals__goal--' + day).addClass('daily-goals__goal--active')
				}
			}
		}
	}

	$rootScope.calculateWeeklyGoal = function() {
		var move = $('.tick-chart__day').slice(0,7)
		var eat = $('.tick-chart__day').slice(7,14)
		var sleep = $('.tick-chart__day').slice(14,21)
		var chart = {'sleep': sleep, 'eat': eat,'move': move}
		for (var activity in chart) {
			var n = 0
			$.each(chart[activity], function (i, v) {
				if($(v).hasClass('.tick-chart__day--active')) {
					n++
					if (n==7) {
						$('.weekly-goals__goal--' + activity).addClass('weekly-goals__goal--active')
					}
				}
			})
		}
	}

	$rootScope.calculateSuperGoal = function() {
		console.log($('.weekly-goals__goal--active').length)
		console.log($('.daily-goals__goal--active').length)
		if ($('.weekly-goals__goal--active').length >= 3 && $('.daily-goals__goal--active').length >= 7)  {
			$('.super-goal').show()
		}
	}

	$rootScope.getNumber = function(num) {
		console.log(num)
	    return new Array(num);   
	}


	//////////
	//////////
	//////////
	//////////

	//authenticate();
	self.credentials = {};
	self.login = function() {
		console.log('self logging in')
		authenticate(self.credentials, function() {
			if ($rootScope.authenticated) {
				$location.path("/");
				self.error = false;
			} else {
				$location.path("/login");
				self.error = true;
			}
		});
	};

	self.logout = function() {
		$http.post('logout', {}).finally(function() {
			$rootScope.authenticated = false;
			$cookies.remove('token');
			$location.path("/");
			$http.defaults.headers.common.token = 'logged_out';
		});
	}
});
/*	.service('api', function() {
		this.getChildren = function() {
			return $http.get('api/child/all', { cache: false })
            .then(function (response) {
            	$log.debug('getOrganisations.then', response.data);
                return response.data;
            });
		}
	})*/