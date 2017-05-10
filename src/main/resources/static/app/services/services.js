angular.module('healthyKids')
	.factory('accountService', ['$http', '$location', '$rootScope', '$cookies', accountService])
	.factory('childService', ['$http', childService]);

function accountService($http, $location, $rootScope, $cookies) {

	return {
		createAccount: function(email, password) {
			var deferred = $q.defer();
			$http.post('account', {email:email, password:password}).then(function(response) {
				var token = response.data.token;
				$rootScope.authenticated = true;
				$cookies.put('token', token);
				$http.defaults.headers.common.token = 'Bearer '+token;
				deferred.resolve(response.data);
			}, function(error) {
				if (error.status === 409) {
					deferred.reject('An account with that email already exists');
				} else {
					deferred.reject("Error: "+error.data.message);
				}
			});
			return deferred.promise;
		},
	
		authenticate: function(credentials) {
			$http({
				method:'POST',
				url: '/login',
				headers: {'Content-Type': 'application/x-www-form-urlencoded'},
				data: $.param(credentials)
	
			}).then(function(response) {
				$rootScope.authenticated = true;
				$cookies.put('token', response.data);
				$http.defaults.headers.common.token = 'Bearer '+response.data;
				$location.path("/child-select");
			}, function() {
				$rootScope.authenticated = false;
			});
	
		},
		
		logout: function() {
			$rootScope.authenticated = false;
			$cookies.put('token', '');
			$http.defaults.headers.common.token = 'Bearer Invalid';
			$location.path("/");
		}
	}
	
} 

function childService($http) {
	return {
		getChild: getChild,
		getChildren: getChildren,
		setAchievement: setAchievement,
		getStickers: getStickers,
		setSticker: setSticker,
		setRewards: setRewards,
		setGoal: setGoal
	};
	
	function getChild(id) {
		return $http.get('api/child/'+id)
			.then(getChildComplete)
			.catch(getChildFailed);
	}
	
	function getChildComplete(response) {
		return response.data;
	}
	
	function getChildFailed(error) {
		console.log(error);
	}
	
	function getChildren() {
		return $http.get('api/child/all').then(
			function(response) {
				return response.data;
			},
			function (error) {
				//
			}
		);

	}
	
	function setAchievement(childId, achievement) {
		return $http.post('api/child/'+childId+'/target', achievement)
				.then(function(response) {}, function(error) {});
	}
	
	function getStickers() {
		return $http.get('api/stickers').then(
				function(response) {
					return response.data;
				},
				function (error) {
					//
				}
			);
	}
	
	function setSticker(childId, sticker) {
		return $http.post('api/child/'+childId+'/sticker', sticker)
		.then(function(response) {}, function(error) {});
	}
	
	function setRewards(childId, rewards) {
		return $http.put('api/child/'+childId+'/reward', rewards)
		.then(function(response) {
			return response.data;
		}, function(error) {
			return error;
		});
	}
	
	function setGoal(childId, goal) {
		return $http.put('api/child/'+childId+'/goal', goal)
		.then(function(response) {
			return response.data;
		}, function(error) {
			return error;
		});
	}
}
