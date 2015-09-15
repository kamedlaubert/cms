'use strict';

angular.module('cmsApp').controller('EnfantDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Enfant', 'Personnel',
        function($scope, $stateParams, $modalInstance, entity, Enfant, Personnel) {

        $scope.enfant = entity;
        $scope.personnels = Personnel.query();
        $scope.load = function(id) {
            Enfant.get({id : id}, function(result) {
                $scope.enfant = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('cmsApp:enfantUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.enfant.id != null) {
                Enfant.update($scope.enfant, onSaveFinished);
            } else {
                Enfant.save($scope.enfant, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
