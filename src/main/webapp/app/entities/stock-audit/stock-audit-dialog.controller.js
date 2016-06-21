(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('StockAuditDialogController', StockAuditDialogController);

    StockAuditDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockAudit', 'Stock'];

    function StockAuditDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockAudit, Stock) {
        var vm = this;
        vm.stockAudit = entity;
        vm.stocks = Stock.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('cashflow6App:stockAuditUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.stockAudit.id !== null) {
                StockAudit.update(vm.stockAudit, onSaveSuccess, onSaveError);
            } else {
                StockAudit.save(vm.stockAudit, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.opDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
