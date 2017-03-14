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
	self.days = [
		{"name":"Mon", "value":0},
		{"name":"Tue", "value":1},
		{"name":"Wed", "value":2},
		{"name":"Thurs", "value":3},
		{"name":"Fri", "value":4},
		{"name":"Sat", "value":5},
		{"name":"Sun", "value":6}
		];
	$http.get('api/child/'+$routeParams.id).then(function(response) {
		console.log(response);
		if (response.data === "") {
			console.log('no data, forwarding home');
			$location.path("/");

		} else {
			console.log('got data');
			self.child = response.data;
		}
	}, function(error) {
		console.log(error);
		console.log('error');
	});
})
.controller('navigation', function($rootScope, $http, $location, $cookies) {

	var self = this
	self.active = false

	var authenticate = function(credentials, callback) {
		console.log("authenticating with credentials ",credentials)

		$http({
			method:'POST',
			url: '/login',
			headers: {'Content-Type': 'application/x-www-form-urlencoded'},
			data: $.param(credentials)

		}).then(function(response) {
			console.log(response);
			console.log(response.data);
			$rootScope.authenticated = true;
			$cookies.put('token', response.data);
			$http.defaults.headers.common.token = 'Bearer '+response.data;
			$location.path("/");
		}, function() {
			$rootScope.authenticated = false;
			callback && callback();
		});

	}

	$rootScope.showChildMenu = function() {
		self.active = !self.active
		if (self.active) {
			$('.child-menu').hide()
		} else {
			$('.child-menu').show()
		}
	}

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