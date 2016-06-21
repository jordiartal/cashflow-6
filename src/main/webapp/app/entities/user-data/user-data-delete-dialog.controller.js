(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('UserDataDeleteController',UserDataDeleteController);

    UserDataDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserData'];

    function UserDataDeleteController($uibModalInstance, entity, UserData) {
        var vm = this;
        vm.userData = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            UserData.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
