(function () {
    'use strict';

    angular
        .module('cashflow6App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('funds', {
                parent: 'entity',
                url: '/funds?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'cashflow6App.funds.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/funds/funds.html',
                        controller: 'FundsController',
                        controllerAs: 'vm'
                    }
                },
                params: {
                    page: {
                        value: '1',
                        squash: true
                    },
                    sort: {
                        value: 'id,asc',
                        squash: true
                    },
                    search: null
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort),
                            search: $stateParams.search
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('funds');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('funds-detail', {
                parent: 'entity',
                url: '/funds/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'cashflow6App.funds.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/funds/funds-detail.html',
                        controller: 'FundsDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('funds');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Funds', function ($stateParams, Funds) {
                        return Funds.get({id: $stateParams.id});
                    }]
                }
            })
            .state('funds.new', {
                parent: 'funds',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/funds/funds-dialog.html',
                        controller: 'FundsDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    isin: null,
                                    nameFund: null,
                                    companyName: null,
                                    actualShares: null,
                                    initialDate: null,
                                    newShares: null,
                                    actualValue: null,
                                    initialValue: null,
                                    newValue: null,
                                    initialShares: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('funds', null, {reload: true});
                    }, function () {
                        $state.go('funds');
                    });
                }]
            })
            .state('funds.edit', {
                parent: 'funds',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/funds/funds-dialog.html',
                        controller: 'FundsDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Funds', function (Funds) {
                                return Funds.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function () {
                        $state.go('funds', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('funds.delete', {
                parent: 'funds',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/funds/funds-delete-dialog.html',
                        controller: 'FundsDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Funds', function (Funds) {
                                return Funds.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function () {
                        $state.go('funds', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('funds.history', {
                parent: 'funds',
                url: '/findAudit/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/funds-audit/history.html',
                        controller: 'FundsAuditHistoryController',
                        controllerAs: 'vm'
                    }
                },
                params: {
                    page: {
                        value: '1',
                        squash: true
                    },
                    sort: {
                        value: 'id,asc',
                        squash: true
                    },
                    search: null
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort),
                            search: $stateParams.search
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('fundsAudit');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'FundsAudit', function ($stateParams, FundsAudit) {
                        return FundsAudit.history({id: $stateParams.id});
                    }]
                }
            })
        ;
    }
})();
