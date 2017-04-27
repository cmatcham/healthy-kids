angular.module('healthyKids')
.controller('login', ['$rootScope', '$http', '$location', '$cookies', 'accountService', loginController]);

function loginController($rootScope, $http, $location, $cookies, accountService) {

	var self = this;
	self.active = true;
	self.login = login;
	self.credentials = {};
	self.submitForm = submitForm;

	var authenticate = function(credentials, callback) {
		accountService.authenticate(credentials);
	}

	function submitForm() {
		$('.login-form__form').submit()
	}
	
	self.credentials = {};
	function login() {
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
};