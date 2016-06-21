(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('CurrencyDeleteController',CurrencyDeleteController);

    CurrencyDeleteController.$inject = ['$uibModalInstance', 'entity', 'Currency'];

    function CurrencyDeleteController($uibModalInstance, entity, Currency) {
        var vm = this;
        vm.currency = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Currency.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
