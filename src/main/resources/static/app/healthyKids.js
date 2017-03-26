angular
    .module('healthyKids', ['ngRoute', 'ngCookies'])
    .config(config)
    .run(run);

function config($routeProvider, $httpProvider) {

	$routeProvider.when('/', {
		templateUrl : 'home.html',
		controller : 'home',
		controllerAs: 'vm'
	}).when('/login', {
		templateUrl : 'login.html',
		controller : 'login',
		controllerAs: 'vm'
	}).when('/child/:id' , {
		templateUrl : 'details.html',
		controller : 'child',
		controllerAs: 'vm'
	}).when('/progress' , {
		templateUrl : 'progress.html',
		controller : 'child',
		controllerAs: 'vm'
	}).when('/category-info' , {
		templateUrl : 'category-info.html',
		controller : 'home',
		controllerAs: 'vm'
	}).when('/sticker' , {
		templateUrl : 'sticker.html',
		controller : 'child',
		controllerAs: 'vm'
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
	
}

function run($rootScope, $http, $cookies, $location) {
	//check if authenticated;
	var token = $cookies.get('token');

	//TODO move this to a service, not root scope
	if (token != null) {
		$http.defaults.headers.common.token = 'Bearer '+token;
		$rootScope.authenticated = true;
	} else {
		$rootScope.authenticated = false;
	}
}
