angular.module('healthyKids')
.controller('login', ['$rootScope', '$http', '$location', '$cookies', 'accountService', loginController]);

function loginController($rootScope, $http, $location, $cookies, accountService) {

	var self = this;
	self.active = true;
	self.login = login;
	self.credentials = {};

	var authenticate = function(credentials, callback) {
		accountService.authenticate(credentials).then(function(success) {
			self.error = !success;
		});
	}
	
	self.credentials = {};
	function login() {
		authenticate(self.credentials, function() {
			if ($rootScope.authenticated) {
				$location.path("/child-select");
				self.error = false;
			} else {
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
};