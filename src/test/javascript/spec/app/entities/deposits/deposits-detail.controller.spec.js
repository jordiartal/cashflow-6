'use strict';

describe('Controller Tests', function() {

    describe('Deposits Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDeposits, MockCurrency, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDeposits = jasmine.createSpy('MockDeposits');
            MockCurrency = jasmine.createSpy('MockCurrency');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Deposits': MockDeposits,
                'Currency': MockCurrency,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("DepositsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'cashflow6App:depositsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
