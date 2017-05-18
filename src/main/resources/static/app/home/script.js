angular.module('healthyKids')
.controller('home', HomeController);

HomeController.$inject = ['$http', '$rootScope', '$cookies', '$location', 'childService', 'accountService'];

function HomeController($http, $rootScope, $cookies, $location, childService, accountService) {
	var self = this;
	
	activate();
	
	function activate() {
		if ($rootScope.authenticated) {
			childService.getChildren().then(function(data) {
				self.children = data;
			});
		} else {
			self.children = [];
		}
	}
	
	self.newChild = {};
	self.newAccount = {};

	self.createChild = createChild;
	self.createAccount = createAccount;
	self.landingScreen = landingScreen;
	self.logout = logout;

	self.showLogin = false

	function logout() {
		accountService.logout()
	}

	function createChild() {
		//TODO get a real dob!
		$http.put('api/child', {firstName:self.newChild.firstName, lastName:self.newChild.lastName, dateOfBirth:'01/01/2000'}).then(function(response) {
			self.children.push(response.data);
		}, function(error) {
			console.log(error);
			console.log('error');
		});
	};

	function landingScreen() {
		setTimeout(function(){
			$('.homepage-background-squares--splash').fadeOut()
			$('.week-nav--home').fadeIn()
			$('.title-text').css({top: '30px'})
		}, 1000);
	}

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

};