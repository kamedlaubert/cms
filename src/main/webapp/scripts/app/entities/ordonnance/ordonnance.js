'use strict';

angular.module('cmsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ordonnance', {
                parent: 'entity',
                url: '/ordonnances',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.ordonnance.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ordonnance/ordonnances.html',
                        controller: 'OrdonnanceController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ordonnance');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('ordonnance.detail', {
                parent: 'entity',
                url: '/ordonnance/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.ordonnance.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ordonnance/ordonnance-detail.html',
                        controller: 'OrdonnanceDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ordonnance');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Ordonnance', function($stateParams, Ordonnance) {
                        return Ordonnance.get({id : $stateParams.id});
                    }]
                }
            })
            .state('ordonnance.new', {
                parent: 'ordonnance',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ordonnance/ordonnance-dialog.html',
                        controller: 'OrdonnanceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {date: null, ligne: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('ordonnance', null, { reload: true });
                    }, function() {
                        $state.go('ordonnance');
                    })
                }]
            })
            .state('ordonnance.edit', {
                parent: 'ordonnance',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ordonnance/ordonnance-dialog.html',
                        controller: 'OrdonnanceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Ordonnance', function(Ordonnance) {
                                return Ordonnance.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('ordonnance', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
