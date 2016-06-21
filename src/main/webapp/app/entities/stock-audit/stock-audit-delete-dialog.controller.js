(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('StockAuditDeleteController',StockAuditDeleteController);

    StockAuditDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockAudit'];

    function StockAuditDeleteController($uibModalInstance, entity, StockAudit) {
        var vm = this;
        vm.stockAudit = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            StockAudit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
