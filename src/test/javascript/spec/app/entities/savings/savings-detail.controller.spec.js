'use strict';

describe('Controller Tests', function() {

    describe('Savings Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSavings, MockCurrency, MockSavingsAudit, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSavings = jasmine.createSpy('MockSavings');
            MockCurrency = jasmine.createSpy('MockCurrency');
            MockSavingsAudit = jasmine.createSpy('MockSavingsAudit');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Savings': MockSavings,
                'Currency': MockCurrency,
                'SavingsAudit': MockSavingsAudit,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("SavingsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'cashflow6App:savingsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
