cdoj
.controller("ArticleEditorController", [
    "$rootScope", "$scope", "$http", "$routeParams", "$window"
    ($rootScope, $scope, $http, $routeParams, $window)->
      $scope.article =
        content: ""
        title: ""
      $scope.fieldInfo = []
      $scope.action = $routeParams.action
      $scope.userName = $routeParams.userName
      if $rootScope.hasLogin == false || ($rootScope.isAdmin == false && $rootScope.currentUser.userName != $scope.userName)
        $window.alert "Permission denied!"
        $window.history.back()

      if $scope.action != "new"
        $scope.title = "Edit article " + $scope.action
        articleId = angular.copy($scope.action)
        $http.get("/article/data/#{articleId}").then (response)->
          data = response.data
          if data.result == "success"
            $scope.article = data.article
          else
            $window.alert data.error_msg
      else
        $scope.title = "New article"

      $scope.submit = ->
        articleEditDTO = angular.copy($scope.article)
        articleEditDTO.action = angular.copy($scope.action)
        articleEditDTO.userName = angular.copy($scope.userName)
        $http.post("/article/edit", articleEditDTO).then (response)->
          data = response.data
          if data.result == "success"
            $window.location.href = "/#/article/show/#{data.articleId}"
          else if data.result == "field_error"
            $scope.fieldInfo = data.field
          else
            $window.alert data.error_msg
  ])