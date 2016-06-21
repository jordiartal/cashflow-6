(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('FundsAuditDeleteController',FundsAuditDeleteController);

    FundsAuditDeleteController.$inject = ['$uibModalInstance', 'entity', 'FundsAudit'];

    function FundsAuditDeleteController($uibModalInstance, entity, FundsAudit) {
        var vm = this;
        vm.fundsAudit = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            FundsAudit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
