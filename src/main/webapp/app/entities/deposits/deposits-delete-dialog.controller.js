(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('DepositsDeleteController',DepositsDeleteController);

    DepositsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Deposits'];

    function DepositsDeleteController($uibModalInstance, entity, Deposits) {
        var vm = this;
        vm.deposits = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Deposits.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
