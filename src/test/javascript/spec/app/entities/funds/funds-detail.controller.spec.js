'use strict';

describe('Controller Tests', function() {

    describe('Funds Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFunds, MockCurrency, MockFundsAudit, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFunds = jasmine.createSpy('MockFunds');
            MockCurrency = jasmine.createSpy('MockCurrency');
            MockFundsAudit = jasmine.createSpy('MockFundsAudit');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Funds': MockFunds,
                'Currency': MockCurrency,
                'FundsAudit': MockFundsAudit,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("FundsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'cashflow6App:fundsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
