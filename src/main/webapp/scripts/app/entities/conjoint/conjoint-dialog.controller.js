'use strict';

angular.module('cmsApp').controller('ConjointDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Conjoint', 'Personnel',
        function($scope, $stateParams, $modalInstance, entity, Conjoint, Personnel) {

        $scope.conjoint = entity;
        $scope.personnels = Personnel.query();
        $scope.load = function(id) {
            Conjoint.get({id : id}, function(result) {
                $scope.conjoint = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('cmsApp:conjointUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.conjoint.id != null) {
                Conjoint.update($scope.conjoint, onSaveFinished);
            } else {
                Conjoint.save($scope.conjoint, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
