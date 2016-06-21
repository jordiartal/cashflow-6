(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('CurrencyDialogController', CurrencyDialogController);

    CurrencyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Currency', 'Savings', 'Deposits', 'Funds', 'Stock'];

    function CurrencyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Currency, Savings, Deposits, Funds, Stock) {
        var vm = this;
        vm.currency = entity;
        vm.savings = Savings.query();
        vm.deposits = Deposits.query();
        vm.funds = Funds.query();
        vm.stocks = Stock.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('cashflow6App:currencyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.currency.id !== null) {
                Currency.update(vm.currency, onSaveSuccess, onSaveError);
            } else {
                Currency.save(vm.currency, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
