(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('StockDeleteController',StockDeleteController);

    StockDeleteController.$inject = ['$uibModalInstance', 'entity', 'Stock'];

    function StockDeleteController($uibModalInstance, entity, Stock) {
        var vm = this;
        vm.stock = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Stock.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
