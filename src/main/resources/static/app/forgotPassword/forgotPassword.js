angular.module('healthyKids')
.controller('forgotPassword', ['$rootScope', '$http', '$location', '$cookies', 'accountService', forgotPasswordController]);

function forgotPasswordController($rootScope, $http, $location, $cookies, accountService) {

	var self = this;
	self.credentials = {};

	self.sendCode = sendCode;
	self.step1 = true;
	self.step2 = false;
	
	self.instructions = "Forget your password? Enter your email address to reset it.";
	
	function sendCode() {
		self.step2 = true;
		self.step1 = false;
		self.instructions = "Code sent to your email. Please enter it here with a new password.";
		
		accountService.resetPassword(self.credentials.username).then(function(data) {
			console.log('data',data);
		}, function(error) {
			console.log('error',error);
		});
		
	}

};