'use strict';

angular.module('cmsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('bPC', {
                parent: 'entity',
                url: '/bPCs',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.bPC.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/bPC/bPCs.html',
                        controller: 'BPCController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('bPC');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('bPC.detail', {
                parent: 'entity',
                url: '/bPC/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.bPC.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/bPC/bPC-detail.html',
                        controller: 'BPCDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('bPC');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'BPC', function($stateParams, BPC) {
                        return BPC.get({id : $stateParams.id});
                    }]
                }
            })
            .state('bPC.new', {
                parent: 'bPC',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/bPC/bPC-dialog.html',
                        controller: 'BPCDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {numeroBpc: null, objectBpc: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('bPC', null, { reload: true });
                    }, function() {
                        $state.go('bPC');
                    })
                }]
            })
            .state('bPC.edit', {
                parent: 'bPC',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/bPC/bPC-dialog.html',
                        controller: 'BPCDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['BPC', function(BPC) {
                                return BPC.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('bPC', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
