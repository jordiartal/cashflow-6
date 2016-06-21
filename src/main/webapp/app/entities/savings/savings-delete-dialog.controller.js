(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('SavingsDeleteController',SavingsDeleteController);

    SavingsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Savings'];

    function SavingsDeleteController($uibModalInstance, entity, Savings) {
        var vm = this;
        vm.savings = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Savings.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
