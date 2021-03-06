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
	}).when('/child/:id/progress' , {
		templateUrl : 'progress.html',
		controller : 'child',
		controllerAs: 'vm'
	}).when('/category-info' , {
		templateUrl : 'category-info.html',
		controller : 'home',
		controllerAs: 'vm'
	}).when('/child/:id/sticker' , {
		templateUrl : 'sticker.html',
		controller : 'child',
		controllerAs: 'vm'
	}).when('/child/:id/rewards' , {
		templateUrl : 'rewards.html',
		controller : 'child',
		controllerAs: 'vm'
	}).when('/child/:id/goals' , {
		templateUrl : 'goals.html',
		controller : 'child',
		controllerAs: 'vm'
	}).when('/child-select' , {
		templateUrl : 'child-select.html',
		controller : 'child',
		controllerAs: 'vm'
	}).when('/child-update' , {
		templateUrl : 'child-update.html',
		controller : 'child',
		controllerAs: 'vm'
	}).when('/child/:id/update' , {
		templateUrl : 'child-update.html',
		controller : 'child',
		controllerAs: 'vm'
	}).otherwise('/');
	
	$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
		
	//initialize get if not there
    if (!$httpProvider.defaults.headers.get) {
        $httpProvider.defaults.headers.get = {};    
    }    
    //disable IE ajax request caching
    $httpProvider.defaults.headers.get['If-Modified-Since'] = 'Mon, 26 Jul 1997 05:00:00 GMT';
    // extra
    $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
    $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';

	$httpProvider.interceptors.push(function($q, $location, $cookies, $rootScope) {
		return {
			'responseError': function(response) {
				console.log('intercepted a bad response, with status so I\'ll send to /login', response.status);
				if (response.status === 401) {			
					$cookies.remove('token');
					console.log('location.pathing to /login');
					$location.path("/login");
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
