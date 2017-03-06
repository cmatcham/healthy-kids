angular.module('hello', [ 'ngRoute' ])
  .config(function($routeProvider, $httpProvider) {

    $routeProvider.when('/', {
      templateUrl : 'home.html',
      controller : 'home',
      controllerAs: 'controller'
    }).when('/login', {
      templateUrl : 'login.html',
      controller : 'navigation',
      controllerAs: 'controller'
    }).otherwise('/');

    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

  })
  .controller('home', function($http) {
    var self = this;
    $http.get('/child/all').then(function(response) {
      self.children = response.data;
    });
    
    self.newChild = {};
    self.newAccount = {};
    
    self.createChild = createChild;
    self.createAccount = createAccount;
    
    function createChild() {
    	console.log('creating a child');
    	$http.put('child', {firstName:self.newChild.firstName, lastName:self.newChild.lastName, dateOfBirth:'01/01/2000'}).then(function(response) {
    		console.log(response);
    	}, function(error) {
    		console.log(error);
    		console.log('error');
    	});
    };
    
    function createAccount() {
    	console.log('creating an account');
    	$http.post('account', {email:self.newAccount.email, password:self.newAccount.password}).then(function(response) {
    		console.log(response);
    	}, function(error) {
    		console.log(error);
    	});
    };
    
  })
  .controller('navigation', function($rootScope, $http, $location) {

	  var self = this
	
	  var authenticate = function(credentials, callback) {
		console.log("authenticating with credentials ",credentials)
	    var headers = credentials ? {authorization : "Basic "
	        + btoa(credentials.username + ":" + credentials.password)
	    } : {};
	
	    $http.get('user', {headers : headers}).then(function(response) {
	      if (response.data.name) {
	        $rootScope.authenticated = true;
	      } else {
	        $rootScope.authenticated = false;
	      }
	      callback && callback();
	    }, function() {
	      $rootScope.authenticated = false;
	      callback && callback();
	    });
	
	  }
	
	  authenticate();
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
			  $location.path("/");
		  });
	  }
	});