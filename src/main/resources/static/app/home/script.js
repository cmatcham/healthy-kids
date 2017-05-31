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
	
	self.confirmPassword = '';

	self.createChild = createChild;
	self.createAccount = createAccount;
	self.landingScreen = landingScreen;
	self.changeInfoTab = changeInfoTab;
	self.scrollTop = scrollTop;
	self.logout = logout;

	self.showLogin = false
	self.landingScreenOpen = true
	self.targets = [
		{'display':'Eat', 'value':'nutrition'},
		{'display':'Move', 'value':'movement'},
		{'display':'Sleep', 'value':'sleep'}
	];

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

	function scrollTop() {
		window.scrollTo(0, 0);
	}

	function changeInfoTab(activity) {
		self.current_activity = activity
		var color = ''
		switch(activity.value) {
		    case 'movement':
		        color = '#7352A2'
		        break;
		    case 'nutrition':
		        color = '#82C341'
		        break;
		    case 'sleep':
		        color = '#EE2A7B'
		        break;
		}
		$('.info-container').css({'background-color': color})
		$('.info-tabs__behind-tabs > div:nth-child(2)').css({'background-color': color})
		$('.info-button').css({'background-color': color})
	}

	function landingScreen() {
		setTimeout(function(){
			self.landingScreenOpen = false
			$('.homepage-background-squares--splash').fadeOut()
			$('.week-nav--home').fadeIn()
			$('.title-text').css({top: '30px'})
		}, 1000);
	}

	function createAccount() {
		if (self.confirmPassword !== self.newAccount.password) {
			console.log(self.confirmPassword, self.newAccount.password);
			self.error = 'Passwords do not match!';
			return;
		}
		$http.post('account', {email:self.newAccount.email, password:self.newAccount.password}).then(function(response) {
			var token = response.data.token;
			$rootScope.authenticated = true;
			$cookies.put('token', token);
			$http.defaults.headers.common.token = 'Bearer '+token;
			self.error = null;
			$location.path("/child-select");
		}, function(error) {
			console.log('Error: ',error);
			console.log('Error.status ', error.status);
			if (error.status === 409) {
				self.error = 'An account with that email already exists. Click below to sign in.';
				console.log(self.error)
			} else if (error.status === 400) {
				self.error = 'Please use a valid email address';
			} else {
				self.error = "Error: "+error.data.message;
				console.log(self.error)
			}
		});
	};

};