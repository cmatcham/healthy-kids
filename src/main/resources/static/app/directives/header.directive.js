angular.module('healthyKids')
	.directive('headerPartial', headerPartial);

function headerPartial() {

	var directive = {
        templateUrl: './app/directives/headerTemplate.html'/*,
        controller: ChildMenuController,
        controllerAs: 'vm',
        bindToController: true */
    };
    
	return directive;

}

/*ChildMenuController.$inject = ['childService'];

function ChildMenuController(childService) {
	var vm = self;
	vm.showChildMenu = false;
}*/