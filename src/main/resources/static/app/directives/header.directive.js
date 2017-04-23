angular.module('healthyKids')
	.directive('headerPartial', headerPartial);

function headerPartial() {

	var directive = {
        templateUrl: './app/directives/headerTemplate.html'
    };
    
	return directive;

}

/*ChildMenuController.$inject = ['childService'];

function ChildMenuController(childService) {
	var vm = self;
	vm.showChildMenu = false;
}*/