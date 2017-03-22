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
	

});
angular.module('healthKids.controllers', []);