(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('StockDialogController', StockDialogController);

    StockDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Stock', 'Currency', 'StockAudit', 'User'];

    function StockDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Stock, Currency, StockAudit, User) {
        var vm = this;
        vm.stock = entity;
        vm.currencies = Currency.query();
        vm.stockaudits = StockAudit.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('cashflow6App:stockUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.stock.id !== null) {
                Stock.update(vm.stock, onSaveSuccess, onSaveError);
            } else {
                Stock.save(vm.stock, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.initialDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
