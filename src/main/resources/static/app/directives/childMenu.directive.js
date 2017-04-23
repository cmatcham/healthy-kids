angular.module('healthyKids')
	.directive('childMenu', ['accountService',childMenu]);

function childMenu(accountService) {

	var directive = {
        templateUrl: './app/directives/childMenuTemplate.html',
        link: function (scope, element, attrs) {
            scope.logout = function () {
                accountService.logout();
            }
        }
    };
    
	return directive;

}