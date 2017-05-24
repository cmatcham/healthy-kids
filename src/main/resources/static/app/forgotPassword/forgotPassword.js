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
		
		accountService.resetPassword(self.credentials.username).then(function(data) {
			if (data.code) {
				self.step2 = true;
				self.step1 = false;
				self.instructions = "Code sent to your email. Please enter it here with a new password.";
			} else {
				self.instructions = "There doesn't seem to be an account with that email.";
			}
		}, function(error) {
			console.log('error',error);
			self.instructions = "There was an error sending an email. Please try again.";
		});
		
	}

};