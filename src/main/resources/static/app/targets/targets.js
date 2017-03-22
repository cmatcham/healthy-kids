angular.module('healthyKids.controllers').controller('Targets', function($rootScope, $http, $location, $routeParams) {
	var self = this;
	self.days = [
		{"name":"Mon", "value":0},
		{"name":"Tue", "value":1},
		{"name":"Wed", "value":2},
		{"name":"Thurs", "value":3},
		{"name":"Fri", "value":4},
		{"name":"Sat", "value":5},
		{"name":"Sun", "value":6}
		];
	$http.get('api/child/'+$routeParams.id).then(function(response) {
		console.log(response);
		if (response.data === "") {
			console.log('no data, forwarding home');
			$location.path("/");

		} else {
			console.log('got data');
			self.child = response.data;
		}
	}, function(error) {
		console.log(error);
		console.log('error');
	});
});