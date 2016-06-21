(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('FundsDeleteController',FundsDeleteController);

    FundsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Funds'];

    function FundsDeleteController($uibModalInstance, entity, Funds) {
        var vm = this;
        vm.funds = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Funds.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
