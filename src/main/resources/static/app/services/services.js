angular.module('healthyKids')
	.factory('accountService', ['$http', '$location', '$rootScope', '$cookies', '$q', accountService])
	.factory('goalService', [goalService])
	.factory('rewardService', [rewardService])
	.factory('childService', ['$http', childService]);

function accountService($http, $location, $rootScope, $cookies, $q) {

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
				} else if (error.status === 400) {
					deferred.reject('Please use a valid email address');
				} else {
					deferred.reject("Error: "+error.data.message);
				}
			});
			return deferred.promise;
		},
	
		authenticate: function(credentials) {
			var deferred = $q.defer();
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
				deferred.resolve(true);
			}, function(error) {
				$rootScope.authenticated = false;
				deferred.resolve(false);
			});
			return deferred.promise;
	
		},
		
		logout: function() {
			$rootScope.authenticated = false;
			$cookies.put('token', '');
			$http.defaults.headers.common.token = 'Bearer Invalid';
			$location.path("/");
		}
	}
	
} 

function rewardService() {

	/**
	 * IMPORTANT:  Do not modify these rewards, or their ids.  New rewards can be added with a higher id
	 * if required.
	 */
	var predefinedRewards = [
			{"id":1,"reward":"Have a picnic at a favourite place."},
			{"id":2,"reward":"Cook your favourite meal."},
			{"id":3,"reward":"Choose a new recipe for the family to make together."},
			{"id":4,"reward":"Go swimming and have an ice cream."},
			{"id":5,"reward":"Watch a movie together."},
			{"id":6,"reward":"Go to the skatepark with your scooter or bike."},
			{"id":7,"reward":"Set up a tenpin bowling alley in the garage and have a family competition."},
			{"id":8,"reward":"Make an indoor tent and have a family tea party with some favourite toys as guests."},
			{"id":9,"reward":"Go to a fun childrenâ€™s activity at our local museum, art gallery, church, or marae."},
			{"id":10,"reward":"Go to the library and bring home books for a family reading session."},
			{"id":11,"reward":"Invite a friend to play."},
			{"id":12,"reward":"Choose a new activity or food for the whole family to enjoy together."},
			{"id":13,"reward":"Choose a small item to buy, like a drawing set or a packet of water balloons."},
	];
	
	return {
		predefinedRewards : predefinedRewards
	};
}

function goalService() {

	/**
	 * IMPORTANT:  Do not modify these goals, or their ids.  New goals can be added with a higher id
	 * if required.
	 * We specify an id on each object rather than just using the array index so that we can delete
	 * elements if necessary without the index of higher values changing.
	 */
	var predefinedGoals = {
			"nutrition":[
				{"id":1,"goal":"Eat either wheat biscuits, porridge, or wholegrain toast for breakfast"},
				{"id":2,"goal":"Try a new porridge topping, like fruit, yoghurt, or nuts"},
				{"id":3,"goal":"Drink milk with breakfast and dinner"},
				{"id":4,"goal":"Drink only water between meal times"},
				{"id":5,"goal":"Try a new type of vegetable or fruit"},
				{"id":8,"goal":"Sit together with the family for dinner"},
				{"id":10,"goal":"Eat three sorts of vegetables at dinner time"}
			],
			"movement":[
				{"id":1,"goal":"Play an active game"},
				{"id":2,"goal":"Learn a new outdoor game"},
				{"id":3,"goal":"Learn a new indoor game"},
				{"id":4,"goal":"Go for a walk"},
				{"id":5,"goal":"Ride a bike or scooter"},
				{"id":6,"goal":"Play outside"},
				{"id":7,"goal":"Go to a new place"},
				{"id":8,"goal":"Make up a new dance"},
				{"id":9,"goal":"Play at the park"},
				{"id":10,"goal":"Watch less television"},
				{"id":11,"goal":"Spend no more than an hour looking at screens, like television or a computer"}
			],
			"sleep":[
				{"id":1,"goal":"Start quiet time one hour before bedtime"},
				{"id":2,"goal":"Start getting ready for bed at 6:30 pm"},
				{"id":3,"goal":"Turn off the television and iPad by 6 pm"},
				{"id":4,"goal":"Plan and keep to a bedtime routine"},
				{"id":5,"goal":"Use a timer to know when to start getting ready for bed and when it's time to turn off the light"},
				{"id":6,"goal":"Read two stories and sing one song"},
				{"id":7,"goal":"Eat crackers or fruit if hungry before bed &mdash; but then brush teeth!"},
				{"id":8,"goal":"Have a drink of water if thirsty before bed"},
				{"id":9,"goal":"Put the light out at 7 pm"},
				{"id":10,"goal":"Stay in bed"}
			]
	};
	
	return {
		predefinedGoals : predefinedGoals
	};
	
	
}

function childService($http) {
	return {
		updateChild: updateChild, 
		getChild: getChild,
		getChildren: getChildren,
		getSummary: getSummary, 
		setAchievement: setAchievement,
		getStickers: getStickers,
		setSticker: setSticker,
		setRewards: setRewards,
		setGoal: setGoal,
		deleteChild: deleteChild
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
	
	function getSummary(child) {
		return $http.get('api/child/'+child.id+'/summary').then(
				function(response) {
					return response.data;
				},
				function (error) {
					//
				}
			);
	}
	
	function deleteChild(child) {
		return $http.delete('api/child/'+child.id).then(
				function(response) {
					return response.data;
				},
				function (error) {
					//
				}
			);
	}
	
	function updateChild(child) {
		if (!child.id) {
			child.id = -1;
		}
		return $http.post('api/child/'+child.id+'/update', child)
		.then(function(response) {return response.data;}, function(error) {});
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
