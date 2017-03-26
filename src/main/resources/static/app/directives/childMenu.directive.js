angular.module('healthyKids')
	.directive('childMenu', childMenu);

function childMenu() {

	var directive = {
        templateUrl: './app/directives/childMenuTemplate.html'/*,
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