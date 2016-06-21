(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('SavingsAuditDeleteController',SavingsAuditDeleteController);

    SavingsAuditDeleteController.$inject = ['$uibModalInstance', 'entity', 'SavingsAudit'];

    function SavingsAuditDeleteController($uibModalInstance, entity, SavingsAudit) {
        var vm = this;
        vm.savingsAudit = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            SavingsAudit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
