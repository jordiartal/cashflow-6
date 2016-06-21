(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('FundsDialogController', FundsDialogController);

    FundsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Funds', 'Currency', 'FundsAudit', 'User'];

    function FundsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Funds, Currency, FundsAudit, User) {
        var vm = this;
        vm.funds = entity;
        vm.currencies = Currency.query();
        vm.fundsaudits = FundsAudit.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('cashflow6App:fundsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.funds.id !== null) {
                Funds.update(vm.funds, onSaveSuccess, onSaveError);
            } else {
                Funds.save(vm.funds, onSaveSuccess, onSaveError);
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
